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

    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    private LiveData<List<Task>> allTasks;
    private LiveData<List<Project>> allProjects;


    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
        allTasks = taskDataSource.getAllTasks();
        allProjects = projectDataSource.getAllProjects();
    }

    public void createTask(long projectId, String name, long creationTimeStamp) {
        executor.execute(() -> {
            taskDataSource.insertTask(new Task(projectId, name, creationTimeStamp));
        });
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> {taskDataSource.deleteTask(taskId);});
    }

    public void updateTask(Task task) {
        executor.execute(() -> {taskDataSource.updateTask(task);});
    }

    public LiveData<List<Task>> getTasks() {
        return allTasks;
    }

    public LiveData<List<Project>> getProjects() {return allProjects;}

    public LiveData<List<String>> getAllProjectsNames() {
        return projectDataSource.getAllProjectsNames();
    }

    public LiveData<Project> getProject(long projectId) {
        return projectDataSource.getProject(projectId);
    }

    public String getName(Task task) {
        return projectDataSource.getName(task.getProjectId());
    }

    public void insertProject(Project project) {
        executor.execute(() -> {projectDataSource.insertProject(project);});
    }

    /*
    //TODO remove
    //TODO: mettre en void après avoir mis un mutablelivedata pour task / post data / observer dans l'activité, pas ici
    public LiveData<List<Task>> getTasks(LifecycleOwner lifecycleOwner, LiveData<List<Project>> Projects){
        LiveData<List<Task>> loadedTasks;
        currentProjects.observe(lifecycleOwner, new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                projectsList = projects;
            }
        });

     */
}


