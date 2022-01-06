package com.cleanup.todoc.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.ToDocDataBase;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {

    private final TaskDao mTaskDao;

    public TaskDataRepository(TaskDao taskDao) {this.mTaskDao=taskDao;}

    public LiveData<List<Task>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getTasksByProject(long projectId) {
        return this.mTaskDao.getTasksByProject(projectId);
    }

    public void insertTask(Task task) {mTaskDao.insertTask(task);}

    public void updateTask(Task task) {mTaskDao.updateTask(task);}

    public void deleteTask(long taskId) {mTaskDao.deleteTask(taskId);}

}