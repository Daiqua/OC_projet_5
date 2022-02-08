package com.cleanup.todoc.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "projectId"))
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long projectId;
    private String name;
    private long creationTimestamp;

    public Task(long projectId, @NonNull String name, long creationTimestamp) {
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    public long getProjectId() {
        return projectId;
    }

    public long getId() {
        return id;
    }

    private void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    private void setName(@NonNull String name) {
        this.name = name;
    }

    private void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

}
