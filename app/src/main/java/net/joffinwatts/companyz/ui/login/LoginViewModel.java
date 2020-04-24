package net.joffinwatts.companyz.ui.login;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.joffinwatts.companyz.callbacks.AccountCreatedCallback;
import net.joffinwatts.companyz.R;
import net.joffinwatts.companyz.callbacks.SuccessfulLoginCallback;
import net.joffinwatts.companyz.data.LoginRepository;
import net.joffinwatts.companyz.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    public static final String LVM = "LoginViewModel";

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LiveData<LoggedInUser> loggedInUser;
    private LiveData<LoggedInUser> createdUserLiveData;
    private LoginRepository loginRepository;
    private MutableLiveData<Boolean> incorrectPassword = new MutableLiveData<>();

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoggedInUser> getLoggedInUser() {
        return loggedInUser;
    }

    public LiveData<Boolean> getIncorrectPassword() {
        return incorrectPassword;
    }

    LiveData<LoggedInUser> getCreatedUserLiveData(){
        return createdUserLiveData;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, @NonNull SuccessfulLoginCallback<Boolean> finishedCallback) {
        // can be launched in a separate asynchronous job

        System.out.println("Attempting login.");
        loggedInUser = loginRepository.signInEmailAndPassword(username, password, finishedCallback);
    }

    public void createUser(LoggedInUser loggedInUser){
        createdUserLiveData = loginRepository.createUserInFirestore(loggedInUser);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void listenForAccountCreation(@NonNull AccountCreatedCallback<Boolean> finishedCallback, LoggedInUser user){
        loginRepository.listenForAccountCreation(finishedCallback, user);
    }



    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public boolean checkForInvalidAttempt(){
        if(loginRepository.incorrectPassword){
            return true;
        }
        return false;
    }
}
