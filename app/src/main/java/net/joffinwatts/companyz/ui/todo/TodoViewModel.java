package net.joffinwatts.companyz.ui.todo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.joffinwatts.companyz.callbacks.LogoutSuccessfulCallback;
import net.joffinwatts.companyz.callbacks.TodoItemInsertedCallback;
import net.joffinwatts.companyz.callbacks.TodosPulledFromFirebaseCallback;
import net.joffinwatts.companyz.data.LoginRepository;
import net.joffinwatts.companyz.data.TodoDataMutator;
import net.joffinwatts.companyz.data.TodoRepository;

import java.util.List;

public class TodoViewModel extends ViewModel {
    private MutableLiveData<List<TodoItem>> todoListLiveData;
    private TodoRepository todoRepository;
    private TodoDataMutator todoDataMutator;
    private LoginRepository loginRepository;

    public static final String TAG = "TodoViewModel";

    TodoViewModel(LoginRepository loginRepository, TodoRepository todoRepository, TodoDataMutator todoDataMutator){
        this.todoRepository = todoRepository;
        this.todoDataMutator = todoDataMutator;
        this.loginRepository = loginRepository;
    }

    LiveData<List<TodoItem>> getTodoListLiveData(){
        return todoListLiveData;
    }

    public void getTodoItems(@NonNull TodosPulledFromFirebaseCallback<Boolean> finishedCallback){
        todoListLiveData = todoRepository.getTodoItems(finishedCallback);
    }

    public void addTodoItem(TodoItem todo, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback){
       todoDataMutator.insertNewTodoIntoFirebase(todo, finishedCallback);
    }

    public void editTodoItem(TodoItem todo, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback){
        todoDataMutator.editTodoInFirebase(todo, finishedCallback);
    }

    public void deleteTodoItem(TodoItem todo, @NonNull TodoItemInsertedCallback<Boolean> finishedCallback){
        todoDataMutator.deleteFromFirebase(todo, finishedCallback);
    }

    public void logout(@NonNull LogoutSuccessfulCallback<Boolean> finishedCallback){
        loginRepository.logout(finishedCallback);
    }
}
