package net.joffinwatts.companyz.data;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import net.joffinwatts.companyz.callbacks.TodoItemInsertedCallback;
import net.joffinwatts.companyz.ui.todo.TodoItem;

public class TodoDataMutator {
    private static volatile TodoDataMutator instance;

    public static final String TAG = "TodoDataInsert";

    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    public TodoDataMutator() { }

    public static TodoDataMutator getInstance(TodoDataMutator todoDataMutator){
        if (instance == null) {
            instance = new TodoDataMutator();
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

    public void editTodoInFirebase(TodoItem todo, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback) {
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("users").document(currentUser.getUid()).collection("todos").document(todo.getFirestoreId()).set(todo).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                System.out.println("Running TodoItemInsertedCallback");
                finishedCallback.callback(task.isComplete());
            }
        });
    }

    public void deleteFromFirebase(TodoItem todo, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback) {
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("users").document(currentUser.getUid()).collection("todos").document(todo.getFirestoreId()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                finishedCallback.callback(task.isComplete());
            }
        });
    }
}
