package net.joffinwatts.companyz.ui.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.joffinwatts.companyz.R;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private List<TodoItem> todoList = new ArrayList<>();

    public TodoListAdapter(){

    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Getting called more than it should
        System.out.println("Inflating new view holder.");
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_todo, null);
        return new TodoViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoViewHolder holder, int position) {
        System.out.println(todoList.toString());
        holder.message.setText(todoList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTodoList(List<TodoItem> list){
        System.out.println("TodoListAdapter | setTodoList");
        todoList.clear();
        todoList = list;
        notifyDataSetChanged();
    }

    public List<TodoItem> getList(){
        return todoList;
    }

    public void addItem(TodoItem todo){
        System.out.println("TodoListAdapter | Adding item to list.");
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public Button editMessage;
        public Button editLocation;

        public TodoViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.message);
            editMessage = itemView.findViewById(R.id.editMessage);
            editLocation = itemView.findViewById(R.id.editLocation);
        }
    }
}
