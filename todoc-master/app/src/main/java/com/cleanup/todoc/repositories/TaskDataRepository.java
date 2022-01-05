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

    //TODO: replace when ready to test - will generate issues
    public TaskDataRepository(Application application) {
        ToDocDataBase db = ToDocDataBase.getInstance(application);
        mTaskDao = db.taskDao();

    }

    public LiveData<List<Task>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getTasksByProject(long projectId) {
        return this.mTaskDao.getTasksByProject(projectId);
    }

    public void insertTask(Task task) {
        ToDocDataBase.databaseWriteExecutor.execute(()->{
            mTaskDao.insertTask(task);
        });

    }

    public void updateTask(Task task) {
        ToDocDataBase.databaseWriteExecutor.execute(()->{
            mTaskDao.updateTask(task);
        });
    }

    public void deleteTask(long taskId) {
        ToDocDataBase.databaseWriteExecutor.execute(()->{
            mTaskDao.deleteTask(taskId);
        });
    }


}