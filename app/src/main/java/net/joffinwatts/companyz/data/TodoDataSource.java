package net.joffinwatts.companyz.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        firestore.collection("users").document(fbAuth.getUid()).collection("todos").addSnapshotListener((snapshot, exception) -> {
            if(exception != null){
                Log.w(TAG, "Listen failed.", exception);
                return;
            }
            if(snapshot != null) {
                for(QueryDocumentSnapshot doc : snapshot){
                    if(doc.get("message") != null){
                        TodoItem todo = new TodoItem(doc.getString("message"));
                        System.out.println(doc.getString("message"));
                        todoList.add(todo);
                    }
                }
            }
        });
        return todoList;
    }


}
