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
    private int numberOfTaskBefore;


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    //public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class); TODO: update

    @Before
    public void init() {
        activity = rule.getActivity();
        lblNoTask = activity.findViewById(R.id.lbl_no_task);
        listTasks = activity.findViewById(R.id.list_tasks);
        //clear the task
        if (listTasks.getAdapter().getItemCount() != 0) {
            while (listTasks.getAdapter().getItemCount() != 0) {
                onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                        .perform(click());
            }
        }
        numberOfTaskBefore = listTasks.getAdapter().getItemCount();
    }

    @Test
    public void NoTaskShouldDisplay_lblNoTask() throws InterruptedException {
        // Check that lblNoTask is displayed
        assertThat(lblNoTask.getVisibility(), Matchers.equalTo(View.VISIBLE));
        // Check that recyclerView is not displayed anymore
        assertThat(listTasks.getVisibility(), Matchers.equalTo(View.GONE));
    }

    @Test
    public void AtLeastOneTaskEShouldDisplay_listTasks() throws InterruptedException {
        //add one task
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaa Tâche example"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        // Check that lblTask is not displayed anymore
        assertThat(lblNoTask.getVisibility(), Matchers.equalTo(View.GONE));
        // Check that recyclerView is displayed
        assertThat(listTasks.getVisibility(), Matchers.equalTo(View.VISIBLE));
    }

    @Test
    public void addOneTaskShouldAddTheDefinedTask() throws InterruptedException {
        //add one task
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaa Tâche example"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check task added
        assertEquals(numberOfTaskBefore + 1, listTasks.getAdapter().getItemCount());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 1, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
    }

    @Test
    public void removeTaskShouldRemoveTheSelectedTask() throws InterruptedException {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("task1"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("task2"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("task3"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks added
        assertEquals(listTasks.getAdapter().getItemCount(), numberOfTaskBefore + 3);
        //remove the middle task
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 2, R.id.img_delete))
                .perform(click());
        //check task removed
        assertEquals(listTasks.getAdapter().getItemCount(), numberOfTaskBefore + 2);
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 1, R.id.lbl_task_name))
                .check(matches(withText("task3")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 2, R.id.lbl_task_name))
                .check(matches(withText("task1")));
    }

    @Test
    public void sortTasksAZ() {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaaab"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaaac"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaaaa"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks created
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore, R.id.lbl_task_name))
                .check(matches(withText("aaaab")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 1, R.id.lbl_task_name))
                .check(matches(withText("aaaac")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 2, R.id.lbl_task_name))
                .check(matches(withText("aaaaa")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaaaa")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("aaaab")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaaac")));
    }

    @Test
    public void sortTasksZA() {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaaay"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaaax"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aaaaz"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks created
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore, R.id.lbl_task_name))
                .check(matches(withText("aaaay")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 1, R.id.lbl_task_name))
                .check(matches(withText("aaaax")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 2, R.id.lbl_task_name))
                .check(matches(withText("aaaaz")));
        // Sort invert alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaaaz")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("aaaay")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaaax")));
    }

    @Test
    public void sortTasksOldestFirst() {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("1st added"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("2nd added"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("3rd added"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks created
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore, R.id.lbl_task_name))
                .check(matches(withText("1st added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 1, R.id.lbl_task_name))
                .check(matches(withText("2nd added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 2, R.id.lbl_task_name))
                .check(matches(withText("3rd added")));
        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("1st added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("2nd added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("3rd added")));
    }

    @Test
    public void sortTasksRecentFirst() {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("first added"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("second added"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("third added"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks created
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore, R.id.lbl_task_name))
                .check(matches(withText("first added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 1, R.id.lbl_task_name))
                .check(matches(withText("second added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 2, R.id.lbl_task_name))
                .check(matches(withText("third added")));
        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("third added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("second added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("first added")));
    }
}
