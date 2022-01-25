package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
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
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void init() {
        projectDataRepository = new ProjectDataRepository(projectDao);
        mutableDummyProjectsList.postValue(dummyProjectsList);

    }

    @Test
    public void getAllProjectsShouldReturnAllProjects() {
        //Given
        when(projectDao.getAllProjects().getValue().size()).thenReturn(2);
        //When
        final int test = projectDataRepository.getAllProjects().getValue().size();
        //Then
        verify(projectDao).getAllProjects();
        assertEquals(test, 2);
    }
}
