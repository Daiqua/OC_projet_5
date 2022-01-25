package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

public class TaskViewModel extends ViewModel implements MainActivity.TasksSortListener {

    private final Executor executor;

    //relative to project
    private final ProjectDataRepository projectRepository;
    public LiveData<List<Project>> liveAllProjects;

    //relative to task
    private final TaskDataRepository taskRepository;
    public LiveData<List<Task>> liveAllTasks;
    private List<Task> taskToSort;

    /**
     * The sort method to be used to display tasks
     */
    private SortMethod sortMethod = SortMethod.NONE;

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

    @Override
    public void getTasksSortListener(int menuItem, MainActivity mainActivity) { //return list task -- suppr listener

        if (menuItem == R.id.filter_alphabetical) { //TODO: implement switch
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (menuItem == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (menuItem == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (menuItem == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }
        sortTasks(sortMethod);
        mainActivity.updateTasks(taskToSort);
    }

    protected enum SortMethod {
        ALPHABETICAL,
        ALPHABETICAL_INVERTED,
        RECENT_FIRST,
        OLD_FIRST,
        NONE
    }

    protected List<Task> sortTasks (SortMethod sortMethodSelected) {
        taskToSort = liveAllTasks.getValue();
        switch (sortMethodSelected) {
            case ALPHABETICAL:
                Collections.sort(taskToSort, new UtilTask.TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(taskToSort, new UtilTask.TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(taskToSort, new UtilTask.TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(taskToSort, new UtilTask.TaskOldComparator());
                break;
        }
        return taskToSort;
    }

}



