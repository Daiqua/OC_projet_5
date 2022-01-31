package com.cleanup.todoc.ui;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.utils.UtilTask;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskViewModel extends ViewModel {

    private final Executor executor;

    //relative to project
    private final ProjectDataRepository projectRepository;

    //relative to task
    private final TaskDataRepository taskRepository;
    private List<Task> taskToSort;

    public TaskViewModel(TaskDataRepository mTaskRepository, ProjectDataRepository mProjectRepository) {
        this.taskRepository = mTaskRepository;
        this.projectRepository = mProjectRepository;
        this.executor = Executors.newFixedThreadPool(2);
    }

    //--- Tasks ---//

    public LiveData<List<Task>> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public void createTask(Task task) {
        executor.execute(() -> taskRepository.insertTask(task));
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> taskRepository.deleteTask(taskId));
    }

    // --- Project ---//

    public LiveData<List<Project>> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    //following method to anticipate future update
    public void insertProject(Project project) {
        executor.execute(() -> projectRepository.insertProject(project));
    }

    // --- sort tasks management --- //

    @SuppressLint("NonConstantResourceId")
    public List<Task> sortTasks(int menuItem, List<Task> tasks) {
        taskToSort = tasks;
        switch (menuItem) {
            case R.id.filter_alphabetical:
                Collections.sort(taskToSort, new UtilTask.TaskAZComparator());
                break;
            case R.id.filter_alphabetical_inverted:
                Collections.sort(taskToSort, new UtilTask.TaskZAComparator());
                break;
            case R.id.filter_recent_first:
                Collections.sort(taskToSort, new UtilTask.TaskRecentComparator());
                break;
            case R.id.filter_oldest_first:
                Collections.sort(taskToSort, new UtilTask.TaskOldComparator());
                break;
        }
        return taskToSort;
    }
}



