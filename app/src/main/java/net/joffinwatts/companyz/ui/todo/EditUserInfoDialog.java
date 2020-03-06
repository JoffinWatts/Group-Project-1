package net.joffinwatts.companyz.ui.todo;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import net.joffinwatts.companyz.R;

public class EditUserInfoDialog extends Dialog {

    public Button saveButton;
    public Button cancelButton;
    public EditText editUsername;

    public EditUserInfoDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_edit_user);
        saveButton = findViewById(R.id.saveTodoEdit);
        cancelButton = findViewById(R.id.cancelTodoEdit);
        editUsername = findViewById(R.id.editTodoText);
    }
}
