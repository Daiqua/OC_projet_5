package com.cleanup.todoc;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

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

    @InjectMocks
    TaskViewModel taskVMTest;

    private final Task task1 = new Task(0, "task1", 0);
    private final Task task2 = new Task(1, "task1", 1);
    private final Task task3 = new Task(2, "task1", 2);
    private final List<Task> dummyTasksList = Arrays.asList(task1, task2);
    private final MutableLiveData<List<Task>> mutableDummyTasksList = new MutableLiveData<>();

    private final Project project1 = new Project("project1", 0); //dao id is 0
    private final Project project2 = new Project("project2", 1);// dao id is 1
    private final Project project3 = new Project("project3", 2);// dao id is 2
    private final List<Project> dummyProjectsList = Arrays.asList(project1, project2);
    private final MutableLiveData<List<Project>> mutableDummyProjectsList = new MutableLiveData<>();



    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        mutableDummyTasksList.setValue(dummyTasksList);
        mutableDummyProjectsList.setValue(dummyProjectsList);
        when(projectRepository.getAllProjects()).thenReturn(mutableDummyProjectsList);
        when(taskRepository.getAllTasks()).thenReturn(mutableDummyTasksList);
        taskVMTest = new TaskViewModel(taskRepository, projectRepository, executor);

    }

    @Test
    public void getAllTasksShouldLoadLiveTasksFromTaskRepository() {
        when(taskRepository.getAllTasks()).thenReturn(mutableDummyTasksList);
        taskVMTest.getAllTasks();
        verify(taskRepository, atLeastOnce()).getAllTasks();
        assertEquals(taskVMTest.getAllTasks(), mutableDummyTasksList);
    }

    @Test
    public void deleteTaskShouldDeleteTheSelectedTask(){
        doNothing().when(taskRepository).deleteTask(task2.getId());
        taskVMTest.deleteTask(task2.getId());
        verify(executor).execute(() -> taskRepository.deleteTask(task2.getId()));

    }

    @Test
    public void createTaskShouldAddTheDefinedTask() {

    }

    @Test
    public void getAllProjectsShouldLoadLiveProjectsFromTaskRepository() {
        when(projectRepository.getAllProjects()).thenReturn(mutableDummyProjectsList);
        taskVMTest.getAllProjects();
        verify(projectRepository, atLeastOnce()).getAllProjects();
        assertEquals(taskVMTest.getAllProjects(), mutableDummyProjectsList);
    }

    @Test
    public void SortTasksAZ(){

    }

    @Test
    public void SortTasksZA(){

    }

    @Test
    public void SortTasksRecentFirst(){

    }

    @Test
    public void SortTasksOldFirst(){

    }

}
