package net.joffinwatts.companyz.ui.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.joffinwatts.companyz.R;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<TodoItem> todoList;
    private TodoViewModel todoViewModel;

    public TodoListAdapter(List<TodoItem> todoList){
        this.todoList = todoList;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_todo, null);
        TodoViewHolder holder = new TodoViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.message.setText(todoList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }
}
