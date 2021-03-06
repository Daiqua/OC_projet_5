package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.ToDocDataBase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    //FOR DATABASE
    private ToDocDataBase mToDocDataBase;

    //DATA FOR TEST
    private final long testProjectOneId = 1;
    private final Project testProjectOne = new Project("TEST_PROJECT1", 0xFFFFFFFF);
    private final Task testTaskOne = new Task(testProjectOneId, "TEST_TASK1", 0);
    private final Task testTaskTwo = new Task(testProjectOneId, "TEST_TASK2", 0);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.mToDocDataBase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                ToDocDataBase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        mToDocDataBase.close();
    }

    // --- ProjectDao tests ---//

    @Test
    public void insertAndGetProjectWithSuccess() throws InterruptedException {
        // BEFORE : Adding a new project
        this.mToDocDataBase.projectDao().insertProject(testProjectOne);
        // TEST
        List<Project> projectInDatabase = LiveDataTestUtil.getValue(this.mToDocDataBase.projectDao().getAllProjects());
        Project project = projectInDatabase.get(0);
        assertTrue(project.getName().equals(testProjectOne.getName()) && project.getId() == testProjectOneId);
    }

    @Test
    public void getAllProjectsWithSuccess() throws InterruptedException {
        // BEFORE : Adding 2 new projects
        this.mToDocDataBase.projectDao().insertProject(testProjectOne);
        this.mToDocDataBase.projectDao().insertProject(testProjectOne);
        // TEST
        List<Project> projectInDatabase = LiveDataTestUtil.getValue(this.mToDocDataBase.projectDao().getAllProjects());
        assertEquals(projectInDatabase.size(), 2);
    }

    // --- TaskDao tests ---//

    @Test
    public void insertAndGetTaskWithSuccess() throws InterruptedException {
        // BEFORE : Adding a new project and a new task
        this.mToDocDataBase.projectDao().insertProject(testProjectOne);
        this.mToDocDataBase.taskDao().insertTask(testTaskOne);
        // TEST
        List<Task> tasksInDatabase = LiveDataTestUtil.getValue(this.mToDocDataBase.taskDao().getAllTasks());
        Task task = tasksInDatabase.get(0);
        long testTaskOneId = 1;
        assertTrue(task.getId() == testTaskOneId && task.getProjectId() == testProjectOneId
                && task.getName().equals(testTaskOne.getName())
                && task.getCreationTimestamp() == testTaskOne.getCreationTimestamp());
    }

    @Test
    public void getAllTasksWithSuccess() throws InterruptedException {
        // BEFORE : Adding 1 new projects and 2 new tasks
        this.mToDocDataBase.projectDao().insertProject(testProjectOne);
        this.mToDocDataBase.taskDao().insertTask(testTaskOne);
        this.mToDocDataBase.taskDao().insertTask(testTaskOne);
        // TEST
        List<Task> tasksInDatabase = LiveDataTestUtil.getValue(this.mToDocDataBase.taskDao().getAllTasks());
        assertEquals(tasksInDatabase.size(), 2);
    }

    @Test
    public void deleteTasksWithSuccess() throws InterruptedException {
        // BEFORE : Adding a new project and a new task
        this.mToDocDataBase.projectDao().insertProject(testProjectOne);
        this.mToDocDataBase.taskDao().insertTask(testTaskOne);
        this.mToDocDataBase.taskDao().insertTask(testTaskTwo);
        //Check the task db has only one tasks
        List<Task> tasksInDatabase = LiveDataTestUtil.getValue(this.mToDocDataBase.taskDao().getAllTasks());
        assertEquals(tasksInDatabase.size(), 2);
        //delete the task and check if removed from the db
        this.mToDocDataBase.taskDao().deleteTask(1);
        tasksInDatabase = LiveDataTestUtil.getValue(this.mToDocDataBase.taskDao().getAllTasks());
        assertEquals(tasksInDatabase.size(), 1);
    }

}
