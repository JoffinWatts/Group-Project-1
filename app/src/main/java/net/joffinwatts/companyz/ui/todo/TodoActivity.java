package net.joffinwatts.companyz.ui.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.joffinwatts.companyz.R;
import net.joffinwatts.companyz.callbacks.TodosPulledFromFirebaseCallback;
import net.joffinwatts.companyz.data.UserInfoMutator;
import net.joffinwatts.companyz.data.model.LoggedInUser;
import net.joffinwatts.companyz.ui.login.LoginActivity;

public class TodoActivity extends AppCompatActivity {

    public static final String TAG = "TodoActivity";

    private TodoViewModel todoViewModel;
    private UserInfoMutator userInfoMutator;
    private TodoListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        LoggedInUser loggedInUser = (LoggedInUser) getIntent().getSerializableExtra("LoggedInUser");
        userInfoMutator = new UserInfoMutator();

        System.out.println("Debugging TodoActivity onCreate");
        todoViewModel = new ViewModelProvider(this, new TodoViewModelFactory()).get(TodoViewModel.class);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

        mAdapter = new TodoListAdapter(this);

        mAdapter.setOnEditMessageListener(message -> {
            // TODO: Tell todoViewModel to start to edit the message in Firebase
            todoViewModel.editTodoItem(message, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    System.out.println("Is successful.");
                    mAdapter.notifyDataSetChanged();
                    hideKeyboard(this);
                }
            });
        });
        mAdapter.setOnDeleteMessageListener(todo -> {
            todoViewModel.deleteTodoItem(todo, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    mAdapter.notifyDataSetChanged();
                }
            });
        });
        mAdapter.setOnCompleteTodoListener(todo -> {
            todoViewModel.deleteTodoItem(todo, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    mAdapter.notifyDataSetChanged();
                }
            });
        });

        mRecyclerView.setAdapter(mAdapter);
        ImageButton addTask = findViewById(R.id.addTask);
        EditText newMessage = findViewById(R.id.newMessage);
        TextView title = findViewById(R.id.title);
        ImageButton accountInfo = findViewById(R.id.accountInfo);
        ImageButton logout = findViewById(R.id.logout);

        if(loggedInUser.getName() != null){
            title.setText(loggedInUser.getName() + "'s todos");
        }

        todoViewModel.getTodoItems(isSuccessful -> {
            if(isSuccessful){
                todoViewModel.getTodoListLiveData().observe(this, list -> {
                    System.out.println("Todo List change observed.");
                    mAdapter.setTodoList(list);
                });
            } else {
                Toast.makeText(this, "Failed to load todo list.", Toast.LENGTH_SHORT).show();
            }
        });

        addTask.setOnClickListener(view -> {
            System.out.println("Adding new todo item.");
            TodoItem dumby = new TodoItem(newMessage.getText().toString());
            todoViewModel.addTodoItem(dumby, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    //returned successful, safe to update adapter
                    System.out.println("TodoItemInsertedCallback returned successful.");
                    mAdapter.addItem(dumby);
                    newMessage.setText("");
                    hideKeyboard(this);
                } else {
                    System.out.println("Something went wrong adding the todo item to Firebase.");
                }
            });
        });

        accountInfo.setOnClickListener(accountButtonView -> {
            EditUserInfoDialog userInfoDialog = new EditUserInfoDialog(this);
            userInfoDialog.cancelButton.setOnClickListener(cancelView -> {
                userInfoDialog.dismiss();
            });
            userInfoDialog.saveButton.setOnClickListener(saveView -> {
                String newUsername = userInfoDialog.editUsername.getText().toString();
                loggedInUser.setName(newUsername);
                userInfoMutator.updateAccountInformation(loggedInUser, isSuccessful -> {
                    if(isSuccessful){
                        System.out.println("Changing activity's title to reflect new username.");
                        title.setText(loggedInUser.getName() + "'s Todos");
                        userInfoDialog.dismiss();
                    }
                });
            });
            userInfoDialog.show();
        });

        logout.setOnClickListener(logoutButtonView -> {
            todoViewModel.logout(isSuccessful -> {
                if(isSuccessful){
                    startLoginActivity();
                }
            });
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void startLoginActivity(){
        Intent intent = new Intent(TodoActivity.this, LoginActivity.class);
        TodoActivity.this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        todoViewModel.logout(isSuccessful -> {
            if(isSuccessful){
                startLoginActivity();
            }
        });
    }
}
