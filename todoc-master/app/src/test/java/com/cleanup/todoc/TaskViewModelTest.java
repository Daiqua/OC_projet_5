package com.cleanup.todoc;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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

@RunWith(JUnit4.class)
@ExtendWith(MockitoExtension.class)
public class TaskViewModelTest {

    @Mock
    private ProjectDataRepository projectRepository;
    @Mock
    private TaskDataRepository taskRepository;

    @InjectMocks
    TaskViewModel taskVMTest;

    //--- task variables ---//
    private final Task task1 = new Task(0, "aaa", 0);
    private final Task task2 = new Task(1, "aab", 1);
    private final Task task3 = new Task(2, "aac", 2);
    //private final List<Task> dummyTasksList = Arrays.asList(task1, task2, task3);
    private final List<Task> alphabeticalAndChronologicalTasksList = Arrays.asList(task1, task2, task3);
    private final List<Task> reverseAlphabeticalAndChronologicalTasksList = Arrays.asList(task3, task2, task1);

    private final MutableLiveData<List<Task>> mutableDummyTasksList = new MutableLiveData<>();
    //--- project variable ---//
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
        //mutableDummyTasksList.setValue(dummyTasksList);
        //mutableDummyProjectsList.setValue(dummyProjectsList);
        //when(projectRepository.getAllProjects()).thenReturn(mutableDummyProjectsList);
        //when(taskRepository.getAllTasks()).thenReturn(mutableDummyTasksList);
        taskVMTest = new TaskViewModel(taskRepository, projectRepository);

    }

    @Test
    public void getAllTasksShouldCallMethodFromTaskRepository() {
        //ViewModel method
        taskVMTest.getAllTasks();
        //checks repository method called
        verify(taskRepository, atLeastOnce()).getAllTasks();
    }

    @Test
    public void deleteTaskShouldCallMethodFromTaskRepository(){
        //ViewModel method
        taskVMTest.deleteTask(task2.getId());
        //checks repository method called
        verify(taskRepository, times(1)).deleteTask(task2.getId());

    }

    @Test
    public void createTaskShouldCallMethodFromTaskRepository() {
        //ViewModel method
        taskVMTest.createTask(task1);
        //checks repository method called
        verify(taskRepository,times(1)).insertTask(task1);
    }

    @Test
    public void getAllProjectsShouldCallMethodFromProjectRepository() {
        //ViewModel method
        taskVMTest.getAllProjects();
        //checks repository method called
        verify(projectRepository, atLeastOnce()).getAllProjects();
    }

    @Test
    public void insertProjectsShouldCallMethodFromProjectRepository() {
        //ViewModel method
        taskVMTest.insertProject(project1);
        //checks repository method called
        verify(projectRepository, times(1)).insertProject(project1);
    }

    @Test
    public void sortTasksShouldSortTasksAlphabeticallyIfAlphabeticalFilterIsSelected(){
        //ViewModel method
        List<Task> testedList = reverseAlphabeticalAndChronologicalTasksList;
        taskVMTest.sortTasks(R.id.filter_alphabetical, testedList);
        //check order
        assertEquals(alphabeticalAndChronologicalTasksList, testedList);
    }

    @Test
    public void sortTasksShouldSortTasksReverseAlphabeticallyIfReverseAlphabeticalFilterIsSelected(){
        //ViewModel method
        List<Task> testedList = alphabeticalAndChronologicalTasksList;
        taskVMTest.sortTasks(R.id.filter_alphabetical_inverted, testedList);
        //check order
        assertEquals(reverseAlphabeticalAndChronologicalTasksList, testedList);
    }

    @Test
    public void sortTasksShouldSortTasksChronologicallyIfChronologicalFilterIsSelected(){
        //ViewModel method
        List<Task> testedList = alphabeticalAndChronologicalTasksList;
        taskVMTest.sortTasks(R.id.filter_recent_first, testedList);
        //check order
        assertEquals(reverseAlphabeticalAndChronologicalTasksList, testedList);
    }

    @Test
    public void sortTasksShouldSortTasksReverseChronologicallyIfReverseChronologicalFilterIsSelected(){
        //ViewModel method
        List<Task> testedList = alphabeticalAndChronologicalTasksList;
        taskVMTest.sortTasks(R.id.filter_oldest_first, testedList);
        //check order
        assertEquals(alphabeticalAndChronologicalTasksList, testedList);
    }

}
