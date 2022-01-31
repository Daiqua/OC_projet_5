package com.cleanup.todoc;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.TestUtils.withRecyclerView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cleanup.todoc.ui.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private TextView lblNoTask;
    private RecyclerView listTasks;
    MainActivity activity;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    //public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class); TODO: update

    @Before
    public void init() {
        activity = rule.getActivity();
        lblNoTask = activity.findViewById(R.id.lbl_no_task);
        listTasks = activity.findViewById(R.id.list_tasks);
        //clear the task
        if (Objects.requireNonNull(listTasks.getAdapter()).getItemCount() != 0) {
            while (listTasks.getAdapter().getItemCount() != 0) {
                onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                        .perform(click());
            }
        }
    }

    @Test
    public void NoTaskShouldDisplay_lblNoTask() {
        // Check that lblNoTask is displayed
        assertThat(lblNoTask.getVisibility(), Matchers.equalTo(View.VISIBLE));
        // Check that recyclerView is not displayed anymore
        assertThat(listTasks.getVisibility(), Matchers.equalTo(View.GONE));
    }

    @Test
    public void AtLeastOneTaskEShouldDisplay_listTasks() {
        //add one task
        addTaskFromHomeScreen("aaa Tâche example");
        // Check that lblTask is not displayed anymore
        assertThat(lblNoTask.getVisibility(), Matchers.equalTo(View.GONE));
        // Check that recyclerView is displayed
        assertThat(listTasks.getVisibility(), Matchers.equalTo(View.VISIBLE));
    }

    @Test
    public void addOneTaskShouldAddTheDefinedTask() {
        //add one task
        addTaskFromHomeScreen("aaa Tâche example");
        //check task added
        assertEquals(1, Objects.requireNonNull(listTasks.getAdapter()).getItemCount());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 1, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }

    @Test
    public void removeTaskShouldRemoveTheSelectedTask() {
        //Add tasks
        addTaskFromHomeScreen("task1");
        addTaskFromHomeScreen("task2");
        addTaskFromHomeScreen("task3");
        //check tasks added
        assertEquals(Objects.requireNonNull(listTasks.getAdapter()).getItemCount(), 3);
        //remove the middle task
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.img_delete))
                .perform(click());
        //check task removed
        assertEquals(listTasks.getAdapter().getItemCount(), 2);
        checkTaskNameAtPosition("task3", 1);
        checkTaskNameAtPosition("task1", 0);
    }

    @Test
    public void sortTasksAZ() {
        //check tasks created
        String taskOneName = "aaaab";
        String taskTwoName = "aaaac";
        String taskThreeName = "aaaaa";
        //Add tasks
        addTaskFromHomeScreen(taskOneName);
        addTaskFromHomeScreen(taskTwoName);
        addTaskFromHomeScreen(taskThreeName);
        //check tasks created and position
        checkTaskNameAtPosition(taskOneName, 0);
        checkTaskNameAtPosition(taskTwoName, 1);
        checkTaskNameAtPosition(taskThreeName, 2);
        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        //check position
        checkTaskNameAtPosition(taskOneName, 1);
        checkTaskNameAtPosition(taskTwoName, 2);
        checkTaskNameAtPosition(taskThreeName, 0);
    }

    @Test
    public void sortTasksZA() {
        String taskOneName = "aaaay";
        String taskTwoName = "aaaax";
        String taskThreeName = "aaaaz";
        //Add tasks
        addTaskFromHomeScreen(taskOneName);
        addTaskFromHomeScreen(taskTwoName);
        addTaskFromHomeScreen(taskThreeName);
        //check tasks created and position
        checkTaskNameAtPosition(taskOneName, 0);
        checkTaskNameAtPosition(taskTwoName, 1);
        checkTaskNameAtPosition(taskThreeName, 2);
        // Sort invert alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        //check position
        checkTaskNameAtPosition(taskOneName, 1);
        checkTaskNameAtPosition(taskTwoName, 2);
        checkTaskNameAtPosition(taskThreeName, 0);
    }

    @Test
    public void sortTasksOldestFirst() {
        String taskOneName = "1st added";
        String taskTwoName = "2nd added";
        String taskThreeName = "3rd added";
        //Add tasks
        addTaskFromHomeScreen(taskOneName);
        addTaskFromHomeScreen(taskTwoName);
        addTaskFromHomeScreen(taskThreeName);
        //check tasks created and position
        checkTaskNameAtPosition(taskOneName, 0);
        checkTaskNameAtPosition(taskTwoName, 1);
        checkTaskNameAtPosition(taskThreeName, 2);
        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        //check position
        checkTaskNameAtPosition(taskOneName, 0);
        checkTaskNameAtPosition(taskTwoName, 1);
        checkTaskNameAtPosition(taskThreeName, 2);
    }

    @Test
    public void sortTasksRecentFirst() {
        String taskOneName = "first added";
        String taskTwoName = "second added";
        String taskThreeName = "third added";
        //Add tasks
        addTaskFromHomeScreen(taskOneName);
        addTaskFromHomeScreen(taskTwoName);
        addTaskFromHomeScreen(taskThreeName);
        //check tasks created and position
        checkTaskNameAtPosition(taskOneName, 0);
        checkTaskNameAtPosition(taskTwoName, 1);
        checkTaskNameAtPosition(taskThreeName, 2);
        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        //check position
        checkTaskNameAtPosition(taskOneName, 2);
        checkTaskNameAtPosition(taskTwoName, 1);
        checkTaskNameAtPosition(taskThreeName, 0);
    }

    private void addTaskFromHomeScreen(String name) {
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText(name))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void checkTaskNameAtPosition(String name, int position) {
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(position, R.id.lbl_task_name))
                .check(matches(withText(name)));
    }

}
