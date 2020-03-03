package net.joffinwatts.companyz.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.joffinwatts.companyz.data.model.LoggedInUser;
import net.joffinwatts.companyz.ui.todo.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoDataSource {

    public static final String TAG = "TodoDataSource";

    private FirebaseFirestore firestore;
    private FirebaseAuth fbAuth;

    public List<TodoItem> getUserTodoItems(){
        System.out.println("TodoDataSource: Getting user todo items.");
        List<TodoItem> todoList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Current UID: " + fbAuth.getUid());
        firestore.collection("users").document(fbAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    //TODO: Somehow check to see if user account is created before pulling data. If not yet, then wait for it or find a listener/observer.
                    System.out.println(task.getResult().getData());
                }
            }
        });
        return todoList;
    }
}
