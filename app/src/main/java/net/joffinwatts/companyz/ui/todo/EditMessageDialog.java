package net.joffinwatts.companyz.ui.todo;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import net.joffinwatts.companyz.R;

public class EditMessageDialog extends Dialog {

    public Button saveButton;
    public Button cancelButton;
    public EditText editMessage;

    public EditMessageDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_edit);
        saveButton = findViewById(R.id.saveTodoEdit);
        cancelButton = findViewById(R.id.cancelTodoEdit);
        editMessage = findViewById(R.id.editTodoText);
    }
}
