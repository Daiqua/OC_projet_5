package com.cleanup.todoc.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    //relative to project
    private final ProjectDataRepository projectRepository;
    private final LiveData<List<Project>> allProjects;
    private final LiveData<List<String>> allProjectsNames;

    //relative to task
    private final TaskDataRepository taskRepository;
    public final MutableLiveData<List<Task>> mutableAllTasks = new MutableLiveData<>();

    public TaskViewModel(Application application) {
        super(application);
        projectRepository = new ProjectDataRepository(application);
        allProjects = projectRepository.getAllProjects();
        allProjectsNames = projectRepository.getAllProjectsNames();

        taskRepository = new TaskDataRepository(application);

    }


    //--- Tasks ---

    public void getAllTasks() {
        LiveData<List<Task>> tasks = taskRepository.getAllTasks();
        mutableAllTasks.postValue(tasks.getValue());
    }



    public void createTask(Task task) {taskRepository.insertTask(task);}

    public void deleteTask(long taskId) {taskRepository.deleteTask(taskId);}

    public void updateTask(Task task) {taskRepository.updateTask(task);}

    public String getProjectNameOfTheTask(Task task) {
        return projectRepository.getName(task.getProjectId());
    }


    // --- Project ---
    public LiveData<List<Project>> getAllProjects() {return allProjects;}

    public LiveData<List<String>> getAllProjectsNames() {return allProjectsNames;}

    public LiveData<Project> getProject(long projectId) {
        return projectRepository.getProject(projectId);
    }

    public void insertProject(Project project) {projectRepository.insertProject(project);}

}


