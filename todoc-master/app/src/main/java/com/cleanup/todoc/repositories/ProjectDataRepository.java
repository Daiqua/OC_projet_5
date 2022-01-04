package com.cleanup.todoc.repositories;


import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao mProjectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.mProjectDao = projectDao;
    }

    public LiveData<List<String>> getAllProjectsNames() {
        return this.mProjectDao.getAllProjectsNames();
    }


    public LiveData<Project> getProject(long projectId) {
        return this.mProjectDao.getProject(projectId);
    }

    public int getColor(long projectId) {
        return this.mProjectDao.getColor(projectId);
    }

    public String getName(long projectId) {
        return this.mProjectDao.getName(projectId);
    }

    public LiveData<List<Project>> getAllProjects() {
        return this.mProjectDao.getAllProjects();
    }

    public void insertProject(Project project) {
        mProjectDao.insert(project);
    }
}