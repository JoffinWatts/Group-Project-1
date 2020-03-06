package net.joffinwatts.companyz.ui.todo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.joffinwatts.companyz.callbacks.TodoItemInsertedCallback;
import net.joffinwatts.companyz.data.TodoDataMutator;
import net.joffinwatts.companyz.data.TodoRepository;

import java.util.List;

public class TodoViewModel extends ViewModel {
    private MutableLiveData<List<TodoItem>> todoListLiveData;
    private TodoRepository todoRepository;
    private TodoDataMutator todoDataMutator;

    public static final String TAG = "TodoViewModel";

    TodoViewModel(TodoRepository todoRepository, TodoDataMutator todoDataMutator){
        this.todoRepository = todoRepository;
        this.todoDataMutator = todoDataMutator;
    }

    LiveData<List<TodoItem>> getTodoListLiveData(){
        return todoListLiveData;
    }

    public void getTodoItems(){
        todoListLiveData = todoRepository.getTodoItems();
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
}
