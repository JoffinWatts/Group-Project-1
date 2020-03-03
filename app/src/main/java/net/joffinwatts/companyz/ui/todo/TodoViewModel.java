package net.joffinwatts.companyz.ui.todo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import net.joffinwatts.companyz.data.TodoRepository;

import java.util.List;

public class TodoViewModel extends ViewModel {
    private LiveData<List<TodoItem>> todoListLiveData;
    private TodoRepository todoRepository;

    public static final String TAG = "TodoViewModel";

    TodoViewModel(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    LiveData<List<TodoItem>> getTodoListLiveData(){
        return todoListLiveData;
    }

    public void getTodoItems(){
        Log.d(TAG, "TodoRepository : Debug 1");
        todoListLiveData = todoRepository.getTodoItems();
    }
}
