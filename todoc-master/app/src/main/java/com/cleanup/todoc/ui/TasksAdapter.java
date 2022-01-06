package com.cleanup.todoc.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 * <p>Adapter which handles the list of tasks to display in the dedicated RecyclerView.</p>
 *
 * @author Gaëtan HERFRAY
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    @NonNull
    private List<Task> tasks;
    private List<Project> projects;
    private List<String> projectsNames;

    @NonNull
    private final DeleteTaskListener deleteTaskListener;


    TasksAdapter(@NonNull final List<Task> tasks, @NonNull final List<Project> projects, @NonNull final DeleteTaskListener deleteTaskListener) {
        this.tasks = tasks;
        this.projects = projects;
        this.deleteTaskListener = deleteTaskListener;

    }

    void updateTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    void updateProjects(List<Project> projects) {
        this.projects = projects;
    }

    void updateProjectsNames(@NonNull final List<String> projectsNames) {
        this.projectsNames = projectsNames;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view, deleteTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface DeleteTaskListener {
        void onDeleteTask(Task task);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        /**
         * The circle icon showing the color of the project
         */
        private final AppCompatImageView imgProject;
        private final TextView lblTaskName;
        private final TextView lblProjectName;
        private final AppCompatImageView imgDelete;
        private final DeleteTaskListener deleteTaskListener;

        TaskViewHolder(@NonNull View itemView, @NonNull DeleteTaskListener deleteTaskListener) {
            super(itemView);

            this.deleteTaskListener = deleteTaskListener;

            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Object tag = view.getTag();
                    if (tag instanceof Task) {
                        TaskViewHolder.this.deleteTaskListener.onDeleteTask((Task) tag);
                    }
                }
            });
        }

        void bind(Task task) {
            Project projectOfTheTask = projects.get(projects.indexOf(task.getProjectId()));
            lblTaskName.setText(task.getName());
            imgDelete.setTag(task);
            imgProject.setSupportImageTintList(ColorStateList.valueOf(projectOfTheTask.getColor()));
            lblProjectName.setText(projectOfTheTask.getName());
        }
    }
}
