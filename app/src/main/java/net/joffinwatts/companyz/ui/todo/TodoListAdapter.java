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

    public TodoListAdapter(){

    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        System.out.println("Inflating new view holder.");
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_todo, null);
        return new TodoViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        System.out.println(todoList.toString());
        holder.message.setText(todoList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTodoList(List<TodoItem> list){
        if(todoList != null){
            todoList.clear();
        }
        todoList = list;
        notifyDataSetChanged();
    }

    public List<TodoItem> getList(){
        return todoList;
    }

    public void addItem(TodoItem todo){
        todoList.add(todo);
        notifyDataSetChanged();
    }
}
