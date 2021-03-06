package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao mProjectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.mProjectDao = projectDao;
    }

    public Project getProject(long projectId) {
        return this.mProjectDao.getProject(projectId);
    }

    public void insertProject(Project project) {
        mProjectDao.insertProject(project);
    }

    public LiveData<List<Project>> getAllProjects() {
        return this.mProjectDao.getAllProjects();
    }
}