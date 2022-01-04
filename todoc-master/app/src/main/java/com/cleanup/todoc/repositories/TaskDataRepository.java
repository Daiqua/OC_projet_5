package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {

    private final TaskDao mTaskDao;

    public TaskDataRepository(TaskDao taskDao) {
        this.mTaskDao = taskDao;
    }

    public LiveData<List<Task>> getAllTasks() {
        return this.mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getTasksByProject(long projectId) {
        return this.mTaskDao.getTasksByProject(projectId);
    }

    public void insertTask(Task task) {
        this.mTaskDao.insertTask(task);
    }

    public void updateTask(Task task) {
        this.mTaskDao.updateTask(task);
    }

    public void deleteTask(long taskId) {
        this.mTaskDao.deleteTask(taskId);
    }


}