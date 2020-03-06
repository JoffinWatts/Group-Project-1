package net.joffinwatts.companyz.ui.todo;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.joffinwatts.companyz.R;
import net.joffinwatts.companyz.data.model.LoggedInUser;

public class TodoActivity extends AppCompatActivity {

    public static final String TAG = "TodoActivity";

    private TodoViewModel todoViewModel;
    private TodoListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        LoggedInUser loggedInUser = (LoggedInUser) getIntent().getSerializableExtra("LoggedInUser");

        System.out.println("Debugging TodoActivity onCreate");
        todoViewModel = new ViewModelProvider(this, new TodoViewModelFactory()).get(TodoViewModel.class);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));

        mAdapter = new TodoListAdapter(this);
        mAdapter.setOnEditMessageListener(message -> {
            // TODO: Tell todoViewModel to start to edit the message in Firebase
            todoViewModel.editTodoItem(message, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    System.out.println("Is successful.");
                    mAdapter.notifyDataSetChanged();
                }
            });
        });
        mAdapter.setOnDeleteMessageListener(todo -> {
            todoViewModel.deleteTodoItem(todo, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    mAdapter.notifyDataSetChanged();
                }
            });
        });

        mRecyclerView.setAdapter(mAdapter);
        ImageButton addTask = findViewById(R.id.addTask);
        EditText newMessage = findViewById(R.id.newMessage);

        todoViewModel.getTodoItems();
        todoViewModel.getTodoListLiveData().observe(this, list -> {
            System.out.println("Todo List change observed.");
            mAdapter.setTodoList(list);
        });

        addTask.setOnClickListener(view -> {
            System.out.println("Adding new todo item.");
            TodoItem dumby = new TodoItem(newMessage.getText().toString());
            todoViewModel.addTodoItem(dumby, isSuccessfulCallback -> {
                if(isSuccessfulCallback){
                    //returned successful, safe to update adapter
                    System.out.println("TodoItemInsertedCallback returned successful.");
                    mAdapter.addItem(dumby);
                } else {
                    System.out.println("Something went wrong adding the todo item to Firebase.");
                }
            });
        });
    }

    private void listenForTodoListData() {
        todoViewModel.getTodoItems();
    }
}
