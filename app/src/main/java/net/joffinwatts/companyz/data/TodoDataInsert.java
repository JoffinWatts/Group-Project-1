package net.joffinwatts.companyz.data;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import net.joffinwatts.companyz.callbacks.TodoItemInsertedCallback;
import net.joffinwatts.companyz.ui.todo.TodoItem;

public class TodoDataInsert {
    private static volatile TodoDataInsert instance;

    public static final String TAG = "TodoDataInsert";

    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    public TodoDataInsert() { }

    public static TodoDataInsert getInstance(TodoDataInsert todoDataInsert){
        if (instance == null) {
            instance = new TodoDataInsert();
        }
        return instance;
    }

    public void insertNewTodoIntoFirebase(TodoItem todo, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback){
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore.collection("users").document(currentUser.getUid()).collection("todos").document().set(todo).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                System.out.println("Running TodoItemInsertedCallback");
                finishedCallback.callback(task.isComplete());
            }
        });
    }
}
