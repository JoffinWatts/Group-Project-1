package net.joffinwatts.companyz.data;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import net.joffinwatts.companyz.callbacks.UserInfoInsertedCallback;
import net.joffinwatts.companyz.data.model.LoggedInUser;

public class UserInfoMutator {
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    public UserInfoMutator() {
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    public void updateAccountInformation(LoggedInUser user, @NonNull UserInfoInsertedCallback<Boolean> finishedCallback){
        firestore.collection("users").document(currentUser.getUid()).set(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                finishedCallback.callback(task.isComplete());
                UserProfileChangeRequest fbProfileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user.getName()).build();
                currentUser.updateProfile(fbProfileUpdates);
            }
        });
    }
}
