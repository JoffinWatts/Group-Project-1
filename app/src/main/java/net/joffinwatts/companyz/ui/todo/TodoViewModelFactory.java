package net.joffinwatts.companyz.ui.todo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.joffinwatts.companyz.data.TodoDataMutator;
import net.joffinwatts.companyz.data.TodoDataSource;
import net.joffinwatts.companyz.data.TodoRepository;

public class TodoViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoViewModel.class)) {
            return (T) new TodoViewModel(TodoRepository.getInstance(new TodoDataSource()), TodoDataMutator.getInstance(new TodoDataMutator()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
