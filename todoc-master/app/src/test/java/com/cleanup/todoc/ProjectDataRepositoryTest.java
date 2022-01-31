package com.cleanup.todoc;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.repositories.ProjectDataRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
@ExtendWith(MockitoExtension.class)
public class ProjectDataRepositoryTest {

    @Mock
    ProjectDao projectDao;

    @InjectMocks
    ProjectDataRepository projectDataRepository;

    private final Project project1 = new Project("project1", 0);//dao id is 0
    private final Project project2 = new Project("project2", 1);// dao id is 1
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
    public void getAllProjectsShouldCallProjectRepositoryMethod() {
        //repository method
        projectDataRepository.getAllProjects();
        //checks dao method called
        verify(projectDao, atLeastOnce()).getAllProjects();
    }

    @Test
    public void getProjectShouldCallProjectRepositoryMethod() {
        //repository method
        Project checkProject1 = projectDataRepository.getProject(0);
        //checks dao method called
        verify(projectDao, atLeastOnce()).getProject(dummyProjectsList.get(0).getId());
    }

    @Test
    public void insertProjectShouldCallProjectRepositoryMethod() {
        //repository method
        projectDataRepository.insertProject(project1);
        //checks dao method called
        verify(projectDao, times(1)).insertProject(project1);
    }

}
