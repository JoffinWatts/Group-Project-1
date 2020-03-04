package net.joffinwatts.companyz.data;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.joffinwatts.companyz.GlobalClass;
import net.joffinwatts.companyz.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static final String TAG = "LoginDataSource";

    private FirebaseAuth mAuth;
    FirebaseFirestore firebase = FirebaseFirestore.getInstance();

    public Result<LoggedInUser> login(String username, String password) {

        try {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener((Activity) GlobalClass.context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "signInWithEmail:success");
                            } else {
                                Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());
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

    public void listenForUserCreation(LoggedInUser user){
        firebase.collection("users").addSnapshotListener((snapshot, exception) -> {
           if(exception != null){
               Log.w(TAG, "Listen failed.", exception);
               return;
           }
           if(snapshot != null &&){
               firestore.collection("users").addSnapshotListener((snapshot2, exception2) -> {
                   if(exception2 != null){
                       Log.w(TAG, "Listen failed.", exception2);
                       return;
                   }
                   QuerySnapshot value = snapshot2;
                   for(QueryDocumentSnapshot doc : snapshot2){
                       if(doc.get("userId").equals(fbAuth.getUid())){
                           System.out.println(doc.getData());
                       }
                   }
               });
           }
        });
    }



    public void logout() {
        // TODO: revoke authentication

    }
}
