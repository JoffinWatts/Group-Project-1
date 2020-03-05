package net.joffinwatts.companyz.ui.todo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.joffinwatts.companyz.callbacks.TodoItemInsertedCallback;
import net.joffinwatts.companyz.data.TodoDataInsert;
import net.joffinwatts.companyz.data.TodoRepository;

import java.util.List;

public class TodoViewModel extends ViewModel {
    private MutableLiveData<List<TodoItem>> todoListLiveData;
    private TodoRepository todoRepository;
    private TodoDataInsert todoDataInsert;

    public static final String TAG = "TodoViewModel";

    TodoViewModel(TodoRepository todoRepository, TodoDataInsert todoDataInsert){
        this.todoRepository = todoRepository;
        this.todoDataInsert = todoDataInsert;
    }

    LiveData<List<TodoItem>> getTodoListLiveData(){
        return todoListLiveData;
    }

    public void getTodoItems(){
        Log.d(TAG, "TodoRepository : Debug 1");
        todoListLiveData = todoRepository.getTodoItems();
    }

    public void addTodoItem(TodoItem todo, TodoListAdapter adapter, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback){
       todoDataInsert.insertNewTodoIntoFirebase(todo, finishedCallback);
    }
}
