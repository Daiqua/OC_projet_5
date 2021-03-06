package com.cleanup.todoc.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Date;
import java.util.List;

public class AddTaskDialog extends AlertDialog {

    private AddTaskListener mAddTaskListener;
    private AlertDialog dialog;
    private EditText dialogEditText;
    private Spinner dialogSpinner;
    private final List<Project> mProjects;

    protected AddTaskDialog(@NonNull Context context, List<Project> projects) {
        super(context);
        mProjects = projects;
        buildAddTaskDialog(context);
        setAddTaskDialog(context);
    }

    private void setAddTaskDialog(Context context) {
        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(view -> onPositiveButtonClick());
        populateDialogSpinner(context);
    }

    private void buildAddTaskDialog(Context context) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.Dialog);
        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        dialog = alertBuilder.create();
        dialog.show();
    }

    private void populateDialogSpinner(Context context) {
        ArrayAdapter<Project> spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, mProjects);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(spinnerAdapter);
        }
    }


    private void onPositiveButtonClick() {
        String taskName = dialogEditText.getText().toString();
        if (taskName.trim().isEmpty()) {
            dialogEditText.setError(this.getContext().getString(R.string.empty_task_name));
        } else {
            Task task = new Task(
                    ((Project) dialogSpinner.getSelectedItem()).getId(),
                    taskName,
                    new Date().getTime()
            );
            mAddTaskListener.getTaskAdded(task);
            this.dialog.dismiss();
        }
    }

    public void setAddTaskListener(AddTaskListener addTaskListener) {
        this.mAddTaskListener = addTaskListener;
    }

    public interface AddTaskListener {

        void getTaskAdded(Task task);
    }
}
