package net.joffinwatts.companyz.data;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.joffinwatts.companyz.GlobalClass;
import net.joffinwatts.companyz.data.model.LoggedInUser;
import net.joffinwatts.companyz.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static final String LDS = "LoginDataSource";

    private FirebaseAuth mAuth;

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener((Activity) GlobalClass.context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(LDS, "signInWithEmail:success");
                            } else {
                                Log.d(LDS, "onComplete: Failed=" + task.getException().getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return new Result.Error(new IOException("Error logging in"));
    }

    private Result.Success<LoggedInUser> LoginSuccess(LoggedInUser user){
        return new Result.Success<LoggedInUser>(user);
    }



    public void logout() {
        // TODO: revoke authentication

    }
}
