package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final Executor executor;

    //relative to project
    private final ProjectDataRepository projectRepository;
    public LiveData<List<Project>> liveAllProjects;
    public Project mProject;

    //relative to task
    private final TaskDataRepository taskRepository;
    public LiveData<List<Task>> liveAllTasks;

    //TODO: review once task loading issue solved to test


    public TaskViewModel(TaskDataRepository mTaskRepository, ProjectDataRepository mProjectRepository, Executor mExecutor) {
        this.taskRepository = mTaskRepository;
        this.projectRepository = mProjectRepository;
        this.executor = mExecutor;
        getAllTasks();
        getAllProjects();
    }


    //--- Tasks ---

    public void getAllTasks() {
        //TODO: review once task loading issue solved to test
        liveAllTasks = taskRepository.getAllTasks();
    }

    public void createTask(Task task) {
        executor.execute(() -> {
            taskRepository.insertTask(task);
        });
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> {
            taskRepository.deleteTask(taskId);
        });
    }

    public void updateTask(Task task) {
        executor.execute(() -> {
            taskRepository.updateTask(task);
        });
    }

    // --- Project ---
    public void getAllProjects() {
        liveAllProjects = projectRepository.getAllProjects();
    }

    public Project getProject(long projectId) {
        return projectRepository.getProject(projectId);
    }

    public void insertProject(Project project) {
        executor.execute(() -> {
            projectRepository.insertProject(project);
        });
    }

}


