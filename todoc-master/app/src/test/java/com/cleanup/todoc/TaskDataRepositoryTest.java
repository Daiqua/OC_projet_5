package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.TaskDataRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskDataRepositoryTest {

    //dummy task DAO
    @Mock
    TaskDao taskDao;

    @InjectMocks
    TaskDataRepository taskDataRepository;

    private final Task task1 = new Task(0, "task1", 0);
    private final Task task2 = new Task(1, "task1", 1);
    private final Task task3 = new Task(2, "task1", 2);

    private List<Task> dummyTasksList = Arrays.asList(task1, task2);

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
        //Mock instruction
        when(taskDao.getAllTasks()).thenReturn(mutableDummyTasksList);
        //load live project form repository
        final LiveData<List<Task>> liveTasks = taskDataRepository.getAllTasks();
        //checks
        verify(taskDao, times(1)).getAllTasks();
        assertEquals(liveTasks.getValue().size(), dummyTasksList.size());
        assertEquals(liveTasks.getValue().get(0), dummyTasksList.get(0));
        assertFalse(liveTasks.getValue().get(0).equals(dummyTasksList.get(1)));
    }

    @Test
    public void insertTaskShouldAddOneTaskFromDao() {
        //TODO check with Brahim - is verify enough for void method?
        //Mock instruction
        //add the task
        taskDataRepository.insertTask(task3);
        //checks
        verify(taskDao, times(1)).insertTask(task3);
        //assertEquals(mutableDummyTasksList.getValue().size(), 3); TODO: remove
        //assertEquals(mutableDummyTasksList.getValue().get(3), task3); TODO: remove
    }

    @Test
    public void deleteTaskShouldRemoveOneTaskTFromDao() {
        //TODO check with Brahim - is verify enough for void method?
        //Mock instruction
        taskDataRepository.deleteTask(task2.getId());
        //checks
        verify(taskDao, times(1)).deleteTask(task2.getId());
    }
}


