package net.joffinwatts.companyz.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.joffinwatts.companyz.ui.todo.TodoItem;

import java.util.List;

public class TodoRepository {
    private static volatile TodoRepository instance;
    public static final String TAG = "TodoRepository";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = firestore.collection("users");

    private TodoDataSource todoDataSource;

    private TodoRepository(TodoDataSource todoDataSource) {
        this.todoDataSource = todoDataSource;
    }

    public static TodoRepository getInstance(TodoDataSource todoDataSource){
        if (instance == null) {
            instance = new TodoRepository(todoDataSource );
        }
        return instance;
    }

    public MutableLiveData<List<TodoItem>> getTodoItems(){
        MutableLiveData<List<TodoItem>> todoListLiveData = new MutableLiveData<>();
        todoListLiveData.setValue(todoDataSource.getUserTodoItems());
        return todoListLiveData;
    }
}
