package com.cleanup.todoc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.TaskDataRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
@ExtendWith(MockitoExtension.class)
public class TaskDataRepositoryTest {

    @Mock
    TaskDao taskDao;

    @InjectMocks
    private TaskDataRepository taskDataRepository;

    private final Task task1 = new Task(0, "task1", 0);
    private final Task task2 = new Task(1, "task1", 1);
    private final Task task3 = new Task(2, "task1", 2);
    private final List<Task> dummyTasksList = Arrays.asList(task1, task2);
    private final MutableLiveData<List<Task>> mutableDummyTasksList = new MutableLiveData<>();


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        taskDataRepository = new TaskDataRepository(taskDao);
        mutableDummyTasksList.setValue(dummyTasksList);
    }

    @Test
    public void getAllProjectsShouldReturnAllProjects() {
        //repository method
        taskDataRepository.getAllTasks();
        //checks dao method called
        verify(taskDao, times(1)).getAllTasks();

    }

    @Test
    public void insertTaskShouldAddOneTaskFromDao() {
        //repository method
        taskDataRepository.insertTask(task3);
        //checks dao method called
        verify(taskDao, times(1)).insertTask(task3);
    }

    @Test
    public void deleteTaskShouldRemoveOneTaskTFromDao() {
        //repository method
        taskDataRepository.deleteTask(task2.getId());
        //checks dao method called
        verify(taskDao, times(1)).deleteTask(task2.getId());
    }
}


