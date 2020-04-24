package net.joffinwatts.companyz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.joffinwatts.companyz.data.model.LoggedInUser;
import net.joffinwatts.companyz.ui.login.LoginActivity;
import net.joffinwatts.companyz.ui.todo.TodoActivity;

//this is a comment
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startApp();
    }

    private void startApp(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            FirebaseUser fbUser = mAuth.getCurrentUser();
            if(fbUser != null){
                LoggedInUser user = new LoggedInUser(fbUser.getUid(), fbUser.getDisplayName(), fbUser.getEmail());
                user.isNew = false;
                startTodoActivity(user);
            } else {
                startLoginActivity();
            }
        }
        else {
            startLoginActivity();
        }
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void startTodoActivity(LoggedInUser loggedInUser){
        Intent intent = new Intent(this, TodoActivity.class);
        intent.putExtra("LoggedInUser", loggedInUser);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
