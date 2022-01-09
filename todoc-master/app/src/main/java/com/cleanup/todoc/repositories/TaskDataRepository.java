package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

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

    public void insertTask(Task task) {
        mTaskDao.insertTask(task);
    }

    public void updateTask(Task task) {
        mTaskDao.updateTask(task);
    }

    public void deleteTask(long taskId) {
        mTaskDao.deleteTask(taskId);
    }

}