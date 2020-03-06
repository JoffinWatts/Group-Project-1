package net.joffinwatts.companyz.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.joffinwatts.companyz.callbacks.TodosPulledFromFirebaseCallback;
import net.joffinwatts.companyz.ui.todo.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoDataSource {

    public static final String TAG = "TodoDataSource";

    private FirebaseFirestore firestore;
    private FirebaseAuth fbAuth;

    public List<TodoItem> getUserTodoItems(@NonNull TodosPulledFromFirebaseCallback<Boolean> finishedCallback){
        System.out.println("TodoDataSource: Getting user todo items.");
        List<TodoItem> todoList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Current UID: " + fbAuth.getUid());
        firestore.collection("users").document(fbAuth.getUid()).collection("todos").addSnapshotListener((snapshot, exception) -> {
            if(exception != null){
                Log.w(TAG, "Listen failed.", exception);
                finishedCallback.callback(false);
                return;
            }
            if(snapshot != null) {
                todoList.clear();
                for(QueryDocumentSnapshot doc : snapshot){
                    if(doc.get("message") != null){
                        TodoItem todo = new TodoItem(doc.getString("message"));
                        System.out.println("Todo item ID: " + doc.getId());
                        todo.setFirestoreId(doc.getId());
                        System.out.println(doc.getString("message"));
                        todoList.add(todo);
                    }
                }
                finishedCallback.callback(true);
            }
        });
        return todoList;
    }


}
