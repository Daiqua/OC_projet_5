package com.cleanup.todoc;

import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.ToDocDataBase;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TaskDaoTest {
/*
    //FOR DATA
    private ToDocDataBase mToDocDataBase;

    //DATA SET FOR TEST

    private final static long TASK_DEMO_ID = 666;
    private final static Task TASK_DEMO = new Task(TASK_DEMO_ID, 1L, "test task", 666);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.mToDocDataBase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                ToDocDataBase.class)
                .allowMainThreadQueries()
                .build();
    }


    @After
    public void closeDb() throws Exception {
        mToDocDataBase.close();
    }

    //TODO: don't work
    @Test
    public void insertAndGetTask() throws InterruptedException {
        // BEFORE : Adding a new project
        this.mToDocDataBase.taskDao().insertTask(TASK_DEMO);
        // TEST
        Task task = (Task) LiveDataTestUtil.getValue(this.mToDocDataBase.taskDao().getTasks(TASK_DEMO_ID));
        assertTrue(task.getName().equals(TASK_DEMO.getName()) && task.getId() == TASK_DEMO_ID);
    }
*/
}
