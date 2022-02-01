package com.cleanup.todoc.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.injection.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener, AddTaskDialog.AddTaskListener {

    // View configuration variable --- //
    private TaskViewModel taskViewModel;
    private TasksAdapter adapter;
    private RecyclerView taskRecyclerView;
    private TextView lblNoTasks;

    // --- sortMethod variable --- //
    private int sortMethodSelected;

    // --- for spinner list --- //
    private List<Project> mProjects;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureView();
        setAddTaskButton();
    }

    // --- LiveData management --- //

    protected void updateTasks(List<Task> liveTasks) {
        if (adapter.getItemCount() == 0) {
            adapter.updateTasks(liveTasks);
        } else {
            adapter.updateTasks(taskViewModel.sortTasks(sortMethodSelected, liveTasks));
        }
        setDisplayOfTasks();
    }

    private void updateProjects(List<Project> liveProjects) {
        mProjects = liveProjects;
        adapter.updateProjects(liveProjects);
    }

    private void observeLiveTasks() {
        taskViewModel.getAllTasks().observe(this, MainActivity.this::updateTasks);
    }

    @Override
    public void onDeleteTask(Task task) {
        this.taskViewModel.deleteTask(task.getId());
    }

    private void addTask(@NonNull Task task) {
        taskViewModel.createTask(task);
    }

    // --- Menu ---//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setSortMethod(item.getItemId()); //this to persist sort after delete or add task
        updateTasks(taskViewModel.sortTasks(sortMethodSelected, adapter.getTasks()));
        return super.onOptionsItemSelected(item);
    }

    private void setSortMethod(int menuItem) {
        sortMethodSelected = menuItem;
    }

    // --- Main View configuration --- //

    private void configureView() {
        setContentView(R.layout.activity_main);
        lblNoTasks = findViewById(R.id.lbl_no_task);
        configureViewModel();
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        taskRecyclerView = findViewById(R.id.list_tasks);
        adapter = new TasksAdapter(this);
        taskRecyclerView.setAdapter(this.adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    private void configureViewModel() {
        this.taskViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance(this)).get(TaskViewModel.class);
        observeLiveTasks();
        taskViewModel.getAllProjects().observe(this, this::updateProjects);
        mProjects = taskViewModel.getAllProjects().getValue(); //todo: check with Brahim: doesnt work if directly put in spinner list(NPE)
    }

    private void setAddTaskButton() {
        findViewById(R.id.fab_add_task).setOnClickListener(view -> {
            AddTaskDialog addTaskDialog = new AddTaskDialog(MainActivity.this, mProjects);
            addTaskDialog.setAddTaskListener(MainActivity.this);
        });
    }

    private void setDisplayOfTasks() {
        if (this.adapter.getItemCount() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            taskRecyclerView.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            taskRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getTaskAdded(Task task) {
        addTask(task);
    }
}
