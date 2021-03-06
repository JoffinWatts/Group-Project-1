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
                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), isSuccessful -> {
                        if(!isSuccessful){
                            Toast.makeText(getApplicationContext(), "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            return;
                        }
                    });
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), isSuccessful -> {
                    if(!isSuccessful){
                        Toast.makeText(getApplicationContext(), "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                });
                loginViewModel.getLoggedInUser().observe(LoginActivity.this, loggedInUser -> {
                    if(loggedInUser.isNew){
                        createNewUser(loggedInUser);
                        loadingProgressBar.setVisibility(View.GONE);
                        startTodoActivity(loggedInUser);
                        Toast.makeText(LoginActivity.this, "Account created! Welcome.", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingProgressBar.setVisibility(View.GONE);
                        startTodoActivity(loggedInUser);
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

    private void startTodoActivity(LoggedInUser loggedInUser){
        Intent intent = new Intent(LoginActivity.this, TodoActivity.class);
        intent.putExtra("LoggedInUser", loggedInUser);
        LoginActivity.this.startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
