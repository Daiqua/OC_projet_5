package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM Task WHERE projectId = :projectId")
    LiveData<List<Task>> getTasksByProject(long projectId);

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("DELETE FROM Task WHERE id = :taskId")
    void deleteTask(long taskId);

}
