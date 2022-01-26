package com.cleanup.todoc;


import static org.junit.Assert.assertEquals;
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mutableDummyTasksList.setValue(dummyTasksList);
        mutableDummyProjectsList.setValue(dummyProjectsList);
        when(projectRepository.getAllProjects()).thenReturn(mutableDummyProjectsList);
        when(taskRepository.getAllTasks()).thenReturn(mutableDummyTasksList);
        TaskViewModel taskVMTest = new TaskViewModel(taskRepository, projectRepository, executor);

    }

    @Test
    public void getAllTasksShouldLoadLiveTasksFromTaskRepository() {
        taskVMTest.getAllTasks();
        verify(taskRepository,times(1)).getAllTasks();
        assertEquals(taskVMTest.liveAllTasks, mutableDummyTasksList);
    }

    @Test
    public void deleteTaskShouldDeleteTheSelectedTask(){

    }

    @Test
    public void createTaskShouldAddTheDefinedTask() {

    }

    @Test
    public void getAllProjectsShouldLoadLiveProjectsFromTaskRepository() {
        taskVMTest.getAllProjects();
        verify(projectRepository,times(1)).getAllProjects();
        assertEquals(taskVMTest.liveAllProjects, mutableDummyProjectsList);
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
