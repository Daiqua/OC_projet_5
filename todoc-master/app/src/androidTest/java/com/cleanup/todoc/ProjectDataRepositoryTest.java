package com.cleanup.todoc;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.repositories.ProjectDataRepository;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)

public class ProjectDataRepositoryTest {

    //dummy project DAO
    @Mock
    private ProjectDao projectDao;

    private ProjectDataRepository projectDataRepository;

    private List<Project> dummyProjectsList = Arrays.asList(
            new Project("test1", 0),
            new Project("test2", 1)
    );
    private MutableLiveData<List<Project>> mutableDummyProjectsList = new MutableLiveData<>();
    LiveData<List<Project>> liveDummyProjectsList = mutableDummyProjectsList;

    @Before
    public void init() {

        mutableDummyProjectsList.postValue(dummyProjectsList);
        LiveData<List<Project>> liveDummyProjectsList = mutableDummyProjectsList;
        projectDataRepository = new ProjectDataRepository(projectDao);


    }

    @Test
    public void getAllProjectsShouldReturnAllProjects() {
        //Given
        when(projectDao.getAllProjects()).thenReturn(liveDummyProjectsList);
        //When
        final LiveData<List<Project>> projectsListLoaded = projectDataRepository.getAllProjects();
        //Then
        verify(projectDao).getAllProjects();
        assertThat(projectsListLoaded.getValue(), Matchers.contains(dummyProjectsList));
    }
}
