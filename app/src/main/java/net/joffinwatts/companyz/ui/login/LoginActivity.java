package net.joffinwatts.companyz.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import net.joffinwatts.companyz.R;
import net.joffinwatts.companyz.data.model.LoggedInUser;
import net.joffinwatts.companyz.ui.todo.TodoActivity;

import java.io.Serializable;

import io.grpc.internal.SerializingExecutor;

public class LoginActivity extends AppCompatActivity {

    public static final String LA = "LoginActivity";

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        System.out.println("Creating login activity");
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Calling afterTextChanged");
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        Log.d(LA, "Creating edit text listeners..");
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                loginViewModel.getLoggedInUser().observe(LoginActivity.this, loggedInUser -> {
                    loadingProgressBar.setVisibility(View.GONE);
                    if(loggedInUser.isNew){
                        createNewUser(loggedInUser);
                        startTodoActivity();
                    } else {
                        startTodoActivity();
                    }
                });
                loginViewModel.getIncorrectPassword().observe(LoginActivity.this, incorrectPassword -> {
                    if(incorrectPassword){
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Invalid password. Pleasse try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateUiWithUser(LoggedInUser user) {
        String welcome = getString(R.string.welcome) + user.getEmail();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    public void createNewUser(LoggedInUser newUser){
        loginViewModel.createUser(newUser);
        loginViewModel.getCreatedUserLiveData().observe(this, user -> {
            if(user.isCreated){
                Toast.makeText(this, "Account created! Welcome.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTodoActivity(){
        Intent intent = new Intent(LoginActivity.this, TodoActivity.class);
        LoginActivity.this.startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
