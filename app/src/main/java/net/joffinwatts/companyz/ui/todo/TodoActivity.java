package net.joffinwatts.companyz.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.joffinwatts.companyz.R;
import net.joffinwatts.companyz.data.model.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    public static final String TAG = "TodoActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TodoViewModel todoViewModel;
    private FloatingActionButton addTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        LoggedInUser loggedInUser = (LoggedInUser) getIntent().getSerializableExtra("LoggedInUser");

        System.out.println("Debugging");
        Log.d(TAG, "TodoRepository : Debug 1");
        todoViewModel = new ViewModelProvider(this, new TodoViewModelFactory()).get(TodoViewModel.class);

        Log.d(TAG, "TodoRepository : Debug 2");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<TodoItem> todoList = new ArrayList<>();

        addTask = findViewById(R.id.addTask);
        setUpAddTaskButton();

        mAdapter = new TodoListAdapter(todoList);
        mRecyclerView.setAdapter(mAdapter);
        todoViewModel.getTodoItems();
    }

    private void listenForTodoListData(){
        Log.d(TAG, "TodoRepository : Debug 1");
        todoViewModel.getTodoItems();
    }

    private void setUpAddTaskButton(){
        addTask.setOnClickListener(view -> {
            TodoItem dumby = new TodoItem("big dumby message");
            todoViewModel.addTodoItem(dumby);
        });
    }

}
