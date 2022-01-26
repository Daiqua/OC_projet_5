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

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cleanup.todoc.ui.MainActivity;

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

    @Before
    public void init() {
        activity = rule.getActivity();
        lblNoTask = activity.findViewById(R.id.lbl_no_task);
        listTasks = activity.findViewById(R.id.list_tasks);
        //count the initial number of tasks
        numberOfTaskBefore = listTasks.getAdapter().getItemCount();

    }


    @Test
    public void addOneTask() throws InterruptedException {
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
    public void removeOneTask() throws InterruptedException {
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
        //remove the 2 other tasks
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 1, R.id.img_delete))
                .perform(click());
        //remove the middle task
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(listTasks.getAdapter().getItemCount() - 1, R.id.img_delete))
                .perform(click());
        assertEquals(listTasks.getAdapter().getItemCount(), numberOfTaskBefore);
    }

    @Test
    public void sortTasksAZ() {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("ab"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("ac"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aa"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks created
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore, R.id.lbl_task_name))
                .check(matches(withText("ab")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 1, R.id.lbl_task_name))
                .check(matches(withText("ac")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 2, R.id.lbl_task_name))
                .check(matches(withText("aa")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aa")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("ab")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("ac")));
        //clear
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());

    }

    @Test
    public void sortTasksZA() {
        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("ab"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("aa"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name))
                .perform(replaceText("ac"))
                .perform(closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //check tasks created
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore, R.id.lbl_task_name))
                .check(matches(withText("ab")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 1, R.id.lbl_task_name))
                .check(matches(withText("aa")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(numberOfTaskBefore + 2, R.id.lbl_task_name))
                .check(matches(withText("ac")));
        // Sort invert alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("ac")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("ab")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aa")));
        //clear
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
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
                .check(matches(withText("3rd added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("2nd added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("1st added")));
        //clear
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());

    }

    @Test
    public void sortTasksRecentFirst() {
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
        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("3rd added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("2nd added")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("1st added")));
        //clear
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                .perform(click());
    }
}
