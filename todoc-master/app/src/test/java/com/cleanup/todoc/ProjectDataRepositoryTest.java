package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.repositories.ProjectDataRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class ProjectDataRepositoryTest {

    @Mock
    ProjectDao projectDao;

    @InjectMocks
    ProjectDataRepository projectDataRepository;

    private final Project project1 = new Project("project1", 0); //dao id is 0
    private final Project project2 = new Project("project2", 1);// dao id is 1
    private final Project project3 = new Project("project3", 2);// dao id is 2

    private final List<Project> dummyProjectsList = Arrays.asList(project1, project2);

    private final MutableLiveData<List<Project>> mutableDummyProjectsList = new MutableLiveData<>();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        projectDataRepository = new ProjectDataRepository(projectDao);
        mutableDummyProjectsList.setValue(dummyProjectsList);
    }

    @Test
    public void getAllProjectsShouldReturnAllProjects() {
        //Mock instruction
        when(projectDao.getAllProjects()).thenReturn(mutableDummyProjectsList);
        //load live project form repository
        final LiveData<List<Project>> liveProjects = projectDataRepository.getAllProjects();
        //checks
        verify(projectDao, times(1)).getAllProjects();
        assertEquals(Objects.requireNonNull(liveProjects.getValue()).size(), dummyProjectsList.size());
        assertEquals(liveProjects.getValue().get(0), dummyProjectsList.get(0));
        assertNotEquals(liveProjects.getValue().get(0), dummyProjectsList.get(1));

    }

    @Test
    public void getProjectShouldReturnTheChosenProject() {
        //Mock instruction
        when(projectDao.getProject(0)).thenReturn(dummyProjectsList.get(0));
        when(projectDao.getProject(1)).thenReturn(dummyProjectsList.get(1));
        //get projects form repository
        Project checkProject1 = projectDataRepository.getProject(0);
        assertEquals(checkProject1, project1);
        Project checkProject2 = projectDataRepository.getProject(1);
        assertEquals(checkProject2, project2);
        verify(projectDao, times(2)).getProject(any(Long.class));
        assertNotEquals(checkProject2, project1);
    }

    @Test
    public void insertProjectShouldAddOneProjectInTheProjectListFromDao() {
        //TODO check with Brahim - is verify enough for void method?
        //Mock instruction
        //add the project
        projectDataRepository.insertProject(project3);
        //checks
        verify(projectDao, times(1)).insertProject(any(Project.class));
        //assertEquals(mutableDummyProjectsList.getValue().size(), 3); TODO: remove
        //assertEquals(mutableDummyProjectsList.getValue().get(3), project3); TODO: remove

    }

}
