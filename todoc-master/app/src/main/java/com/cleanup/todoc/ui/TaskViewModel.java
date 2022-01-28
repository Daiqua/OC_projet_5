package com.cleanup.todoc.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
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

public class TaskViewModel extends ViewModel {

    private final Executor executor;

    //relative to project
    private final ProjectDataRepository projectRepository;
    public LiveData<List<Project>> liveAllProjects;

    //relative to task
    private final TaskDataRepository taskRepository;
    public LiveData<List<Task>> liveAllTasks;
    private List<Task> taskToSort;

    public TaskViewModel(TaskDataRepository mTaskRepository, ProjectDataRepository mProjectRepository, Executor mExecutor) {
        this.taskRepository = mTaskRepository;
        this.projectRepository = mProjectRepository;
        this.executor = mExecutor;
        getAllTasks();
        getAllProjects();
    }

    //--- Tasks ---//

    public void getAllTasks() {
        liveAllTasks = taskRepository.getAllTasks();
    }

    public void createTask(Task task) {
        executor.execute(() -> taskRepository.insertTask(task));
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> taskRepository.deleteTask(taskId));
    }

    // --- Project ---//

    public void getAllProjects() {
        liveAllProjects = projectRepository.getAllProjects();
    }

    //following method to anticipate future update
    public void insertProject(Project project) {
        executor.execute(() -> projectRepository.insertProject(project));
    }

    // --- sort tasks management --- //

    @SuppressLint("NonConstantResourceId")
    protected void sortTasks( int menuItem, MainActivity mainActivity) {
        taskToSort = liveAllTasks.getValue();
        switch (menuItem) {
            case R.id.filter_alphabetical:
                Collections.sort(taskToSort, new UtilTask.TaskAZComparator());
                break;
            case R.id.filter_alphabetical_inverted:
                Collections.sort(taskToSort, new UtilTask.TaskZAComparator());
                break;
            case R.id.filter_oldest_first:
                Collections.sort(taskToSort, new UtilTask.TaskRecentComparator());
                break;
            case R.id.filter_recent_first:
                Collections.sort(taskToSort, new UtilTask.TaskOldComparator());
                break;
        }
        mainActivity.updateTasks(taskToSort);
    }



}



