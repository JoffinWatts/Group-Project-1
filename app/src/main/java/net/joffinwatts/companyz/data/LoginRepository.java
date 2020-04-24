package net.joffinwatts.companyz.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.joffinwatts.companyz.callbacks.AccountCreatedCallback;
import net.joffinwatts.companyz.GlobalClass;
import net.joffinwatts.companyz.callbacks.LogoutSuccessfulCallback;
import net.joffinwatts.companyz.callbacks.SuccessfulLoginCallback;
import net.joffinwatts.companyz.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    public static final String TAG = "LoginRepository";

    public boolean incorrectPassword = false;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = firestore.collection("users");

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout(@NonNull LogoutSuccessfulCallback<Boolean> finishedCallback) {
        if(user != null) {
            user = null;
        }
        dataSource.logout(finishedCallback);
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public MutableLiveData<LoggedInUser> createUserInFirestore(final LoggedInUser loggedInUser){
        //TODO: Make this not get called twice. Might be causing accounts to not get created in FireStore.
        System.out.println("Creating new user");
        final MutableLiveData<LoggedInUser> newUser = new MutableLiveData<>();
        final DocumentReference uidRef = usersRef.document(loggedInUser.getUserId());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if(uidTask.isSuccessful()){
                DocumentSnapshot document = uidTask.getResult();
                if(!document.exists()){
                    uidRef.set(loggedInUser).addOnCompleteListener(userCreationTask -> {
                        if(userCreationTask.isSuccessful()){
                            loggedInUser.isCreated = true;
                            newUser.setValue(loggedInUser);
                        } else {
                            Log.d(TAG, userCreationTask.getException().getMessage());
                        }
                    });
                } else {
                    newUser.setValue(loggedInUser);
                }
            } else {
                Log.d(TAG, uidTask.getException().getMessage());
            }
        });

        return newUser;
    }

    public MutableLiveData<LoggedInUser> signInEmailAndPassword(String username, String password, @NonNull SuccessfulLoginCallback<Boolean> finishedCallback) {
        final MutableLiveData<LoggedInUser> authenticatedUser = new MutableLiveData<>();
        fbAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {
                        createUserObject(authTask, authenticatedUser);
                        finishedCallback.callback(authTask.isComplete());
                    } else {
                        Log.d(TAG, "onComplete: Failed=" + authTask.getException());
                        fbAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(accountCreation -> {
                            if (accountCreation.isSuccessful()) {
                                createUserObject(accountCreation, authenticatedUser);
                                finishedCallback.callback(accountCreation.isComplete());
                            } else {
                                //Password was invalid.
                                finishedCallback.callback(accountCreation.isSuccessful());
                            }
                        });
                    }
                });
        return authenticatedUser;
    }

    private void createUserObject(Task<AuthResult> task, MutableLiveData<LoggedInUser> authenticatedUser){
        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        if(fbUser != null){
            LoggedInUser user = new LoggedInUser(fbUser.getUid(), fbUser.getDisplayName(), fbUser.getEmail());
            user.isNew = isNewUser;
            authenticatedUser.setValue(user);
            setLoggedInUser(user);
            if(user.isNew){
                createUserInFirestore(user);
            }
        }
    }

    public void listenForAccountCreation(@NonNull AccountCreatedCallback<Boolean> finishedCallback, LoggedInUser user){
        dataSource.listenForUserCreation(finishedCallback, user);
    }
}
