package net.joffinwatts.companyz.ui.todo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.joffinwatts.companyz.data.TodoDataInsert;
import net.joffinwatts.companyz.data.TodoDataSource;
import net.joffinwatts.companyz.data.TodoRepository;

public class TodoViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoViewModel.class)) {
            return (T) new TodoViewModel(TodoRepository.getInstance(new TodoDataSource()), TodoDataInsert.getInstance(new TodoDataInsert()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
