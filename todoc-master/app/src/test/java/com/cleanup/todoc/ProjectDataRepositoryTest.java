package com.cleanup.todoc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.repositories.ProjectDataRepository;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProjectDataRepositoryTest {

    //dummy project DAO
    @Mock
    ProjectDao projectDao;

    @InjectMocks
    ProjectDataRepository projectDataRepository;

    private List<Project> dummyProjectsList = Arrays.asList(
            new Project("test1", 0),
            new Project("test2", 1)
    );
    private MutableLiveData<List<Project>> mutableDummyProjectsList = new MutableLiveData<>();
    private MutableLiveData<List<Project>> mutableDummyProjectsList2 = new MutableLiveData<>();

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
        verify(projectDao).getAllProjects();
        assertEquals(liveProjects.getValue().size(), dummyProjectsList.size());
        assertEquals(liveProjects.getValue().get(0), dummyProjectsList.get(0));
        assertFalse(liveProjects.getValue().get(0).equals(dummyProjectsList.get(1)));

    }
}
