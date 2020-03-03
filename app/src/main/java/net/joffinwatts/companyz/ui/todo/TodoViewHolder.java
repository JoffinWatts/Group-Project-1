package net.joffinwatts.companyz.ui.todo;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.joffinwatts.companyz.R;

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
