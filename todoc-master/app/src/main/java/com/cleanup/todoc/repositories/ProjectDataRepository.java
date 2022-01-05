package com.cleanup.todoc.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ToDocDataBase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao mProjectDao;
    private final LiveData<List<Project>> allProjects;
    private final LiveData<List<String>> allProjectsNames;
    private LiveData<Project> projectById;
    private int projectColor;
    private String projectName;

    //TODO: replace when ready to test - will generate issues
    public ProjectDataRepository(Application application) {
        ToDocDataBase db = ToDocDataBase.getInstance(application);
        mProjectDao = db.projectDao();
        allProjects = mProjectDao.getAllProjects();
        allProjectsNames = mProjectDao.getAllProjectsNames();

    }

    public LiveData<List<String>> getAllProjectsNames() {
        return allProjectsNames;
    }

    public LiveData<Project> getProject(long projectId) {
        ToDocDataBase.databaseWriteExecutor.execute(()->{
            projectById = mProjectDao.getProject(projectId);
        });

        return projectById;
    }

    public int getColor(long projectId){
        ToDocDataBase.databaseWriteExecutor.execute(()->{
            projectColor = mProjectDao.getColor(projectId);
        });

        return projectColor;
    }
    public String getName(long projectId) {
        ToDocDataBase.databaseWriteExecutor.execute(()->{
            projectName = mProjectDao.getName(projectId);
        });

        return projectName;
    }

    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insertProject(Project project) {
            ToDocDataBase.databaseWriteExecutor.execute(()->{
                mProjectDao.insert(project);
            });
    }
}