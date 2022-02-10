package com.cleanup.todoc.injection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.database.ToDocDataBase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ProjectDataRepository projectRepository;
    private final TaskDataRepository taskRepository;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(Context context) {
        ToDocDataBase dataBase = ToDocDataBase.getInstance(context);
        this.taskRepository = new TaskDataRepository(dataBase.taskDao());
        this.projectRepository = new ProjectDataRepository(dataBase.projectDao());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(taskRepository, projectRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
