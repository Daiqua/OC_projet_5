package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {


    private TaskViewModel taskViewModel;

    @NonNull
    private List<Task> tasks;
    private final ArrayList<Project> projects = new ArrayList<>();
    private final ArrayList<String> projectsNames = new ArrayList<>();

    private TasksAdapter adapter;

    /**
     * The sort method to be used to display tasks
     */
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;

    @Nullable
    public AlertDialog dialog = null;

    @Nullable
    private EditText dialogEditText = null;

    @Nullable
    private Spinner dialogSpinner = null;

    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView taskRecyclerView;

    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();//can be before oncreate
        taskRecyclerView = findViewById(R.id.list_tasks);
        adapter = new TasksAdapter(tasks, projects, this);
        taskRecyclerView.setAdapter(this.adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.mutableAllTasks.observe(this, mtasks -> {
            updateUI(mtasks);
        });

        taskViewModel.getAllTasks();

        lblNoTasks = findViewById(R.id.lbl_no_task);

        findViewById(R.id.fab_add_task).setOnClickListener(view -> {

            //TODO: remove
            Toast.makeText(getApplicationContext(), "projet:"+taskViewModel.getAllProjectsNames(), Toast.LENGTH_SHORT).show();

            showAddTaskDialog();
        });
    }

    private void updateUI(List<Task> tasks){
        tasks.addAll(tasks);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    /*
    //TODO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }

        adapter.updateTasks();

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onDeleteTask(Task task) {
        this.taskViewModel.deleteTask(task.getId());
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                // TODO: Replace this by id of persisted task
                long id = (long) (Math.random() * 50000);


                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else {
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    private void addTask(@NonNull Task task) {
        //TODO
    }


    private void updateTasks() {
        if (this.adapter.getItemCount()==0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            taskRecyclerView.setVisibility(View.GONE);
        } else {

            lblNoTasks.setVisibility(View.GONE);
            taskRecyclerView.setVisibility(View.VISIBLE);
/*
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new UtilTask.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new UtilTask.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new UtilTask.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new UtilTask.TaskOldComparator());
                    break;
            }
            adapter.updateTasks();
 */
        }
    }
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    private void populateDialogSpinner() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projectsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }
    //TODO: remove/organize?
    private void updateProjects (List<Project> projects){
        this.adapter.updateProjects(projects);
    }

    //TODO: remove/organize?
    private void getAllProjects(){
        this.taskViewModel.getAllProjects().observe(this, this::updateProjects);
    }
}
