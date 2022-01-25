package com.cleanup.todoc;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

@ExtendWith(MockitoExtension.class)
public class TaskViewModelTest {

    @Mock
    private ProjectDataRepository projectRepository;
    @Mock
    private TaskDataRepository taskRepository;
    @Mock
    private Executor executor;

    TaskViewModel taskVMTest;
    private MutableLiveData<List<Task>> dummyLiveTasks = new MutableLiveData<>();
    private List<Task> dummyTasks = Arrays.asList(
            new Task(1, "test1", 1),
            new Task(2, "test2", 2)
    );



@BeforeEach
public void init(){
    TaskViewModel taskVMTest = new TaskViewModel(taskRepository, projectRepository, executor);
}

@Test
public void getAllTasksShouldLoadLiveTasks(){
    dummyLiveTasks.postValue(dummyTasks);
    when(taskRepository.getAllTasks()).thenReturn(dummyLiveTasks);
    taskVMTest.getAllTasks();
    final LiveData<List<Task>> test = taskVMTest.liveAllTasks;
    verify(taskRepository).getAllTasks();
    assertEquals(test, dummyLiveTasks);

}

}
