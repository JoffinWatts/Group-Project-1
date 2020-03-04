package net.joffinwatts.companyz.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public boolean insertNewTodoIntoFirebase(TodoItem todo){
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firestore.collection("users").document(currentUser.getUid()).collection("todos").document().set(todo).isSuccessful()){
            return true;
        }
        return false;
    }
}
