package net.joffinwatts.companyz.ui.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.joffinwatts.companyz.R;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private List<TodoItem> todoList = new ArrayList<>();
    private Context context;

    private OnEditMessageListener onEditMessageListener;
    private OnDeleteMessageListener onDeleteMessageListener;
    private OnCompleteTodoListener onCompleteTodoListener;

    public TodoListAdapter(Context context){
        this.context = context;
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
        holder.completeTodo.setChecked(false);
        holder.message.setText(todoList.get(position).getMessage());
        holder.itemView.setOnClickListener(view -> {
            System.out.println("User prompted to edit the message.");
            EditMessageDialog editDialog = new EditMessageDialog(context);

            editDialog.saveButton.setOnClickListener(saveView -> {
                System.out.println("User saved new todo.");
                TodoItem newTodo = new TodoItem(editDialog.editMessage.getText().toString());
                newTodo.setFirestoreId(todoList.get(position).getFirestoreId());
                onEditMessageListener.onEditMessageSaved(newTodo);
                editDialog.dismiss();
            });

            editDialog.cancelButton.setOnClickListener(cancelView -> {
                System.out.println("User cancelled dialog.");
                editDialog.dismiss();
            });

            editDialog.show();

        });
        holder.deleteMessage.setOnClickListener(deleteView -> {
            TodoItem todo = todoList.get(position);
            //TODO: Delete from todoList if neccessary
            onDeleteMessageListener.onDeleteMessage(todo);
        });
        holder.completeTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    TodoItem todo = todoList.get(position);
                    onCompleteTodoListener.onCompleteTodo(todo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTodoList(List<TodoItem> list){
        System.out.println("TodoListAdapter | setTodoList");
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
        public ImageButton deleteMessage;
        public CheckBox completeTodo;

        public TodoViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.message);
            deleteMessage = itemView.findViewById(R.id.deleteMessage);
            completeTodo = itemView.findViewById(R.id.completeCheckbox);
        }
    }

    //Listens for when the user clicks the edit button and notifies the Activity to edit the record in the DB and update UI.
    public interface OnEditMessageListener {
        void onEditMessageSaved(TodoItem todo);
    }

    public void setOnEditMessageListener(OnEditMessageListener onEditMessageListener){
        this.onEditMessageListener = onEditMessageListener;
    }

    //Listens for when the user clicks the delete button and notifies the Activity to start r
    public interface OnDeleteMessageListener {
        void onDeleteMessage(TodoItem todo);
    }

    public void setOnDeleteMessageListener(OnDeleteMessageListener onDeleteMessageListener){
        this.onDeleteMessageListener = onDeleteMessageListener;
    }

    public interface OnCompleteTodoListener {
        void onCompleteTodo(TodoItem todo);
    }

    public void setOnCompleteTodoListener(OnCompleteTodoListener onCompleteTodoListener){
        this.onCompleteTodoListener = onCompleteTodoListener;
    }

}
