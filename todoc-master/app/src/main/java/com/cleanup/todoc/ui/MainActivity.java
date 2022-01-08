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
import com.cleanup.todoc.injection.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.utils.UtilTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    // View configuration variable --- //
    private TaskViewModel taskViewModel;
    private TasksAdapter adapter;
    private RecyclerView taskRecyclerView;
    private TextView lblNoTasks;
    /**
     * The sort method to be used to display tasks
     */
    private SortMethod sortMethod = SortMethod.NONE;

    // Objects variable --- //
    //private List<Task> tasks = new ArrayList<>();
    //private List<Project> projects = new ArrayList<>();

    // --- AddTask dialog variable
    public AlertDialog dialog = null;
    private EditText dialogEditText = null;
    private Spinner dialogSpinner = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        configureViewModel();
        configureRecyclerView();
        lblNoTasks = findViewById(R.id.lbl_no_task);

        findViewById(R.id.fab_add_task).setOnClickListener(view -> {
            showAddTaskDialog();
        });
    }

    private void updateTasks(List<Task> liveTasks) {
        adapter.updateTasks(liveTasks);
        setDisplayOfTasks();
    }

    private void updateProjects(List<Project> liveProjects) {
        adapter.updateProjects(liveProjects);
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

    // --- Main View configuration --- //

    private void configureRecyclerView() {
        taskRecyclerView = findViewById(R.id.list_tasks);
        adapter = new TasksAdapter(this);
        taskRecyclerView.setAdapter(this.adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void configureViewModel() {
        this.taskViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance(this)).get(TaskViewModel.class);
        taskViewModel.liveAllTasks.observe(this, this::updateTasks);
        taskViewModel.liveAllProjects.observe(this, this::updateProjects);

    }

    private void setDisplayOfTasks() {
        if (this.adapter.getItemCount() == 0) {
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

 */
        }
    }

    private enum SortMethod {
        ALPHABETICAL,
        ALPHABETICAL_INVERTED,
        RECENT_FIRST,
        OLD_FIRST,
        NONE
    }

    // ---- AddTask Dialog configuration ---- //

    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();
        dialog.show();
        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);
        populateDialogSpinner();

    }

    @NonNull
    private AlertDialog getAddTaskDialog() {
        buildAddTaskDialog();
        setOnShowListener();
        return dialog;
    }

    private void buildAddTaskDialog() {

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
    }

    private void setOnShowListener() {
        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                //TODO: remove once project spinner list correctly implemented
                Toast.makeText(getApplicationContext(), "Task VM" + taskViewModel.liveAllTasks.getValue().size() + " adapter" + adapter.getItemCount(), Toast.LENGTH_SHORT).show();

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project projectOfTheTask = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                projectOfTheTask = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (projectOfTheTask != null) {

                Task task = new Task(
                        projectOfTheTask.getId(),
                        taskName,
                        new Date().getTime()
                );

                //TODO: review once mutableData correctly implemented
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

    private void addTask(@NonNull Task task) {
        //TODO: review once mutableData correctly implemented
        this.taskViewModel.createTask(task);

    }

    private void populateDialogSpinner() {
        ArrayAdapter<Project> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, taskViewModel.liveAllProjects.getValue() );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(spinnerAdapter);
        }
    }


}
