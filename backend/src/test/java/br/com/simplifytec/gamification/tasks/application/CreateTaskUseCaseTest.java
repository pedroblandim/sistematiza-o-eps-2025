package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private CreateTaskUseCase createTaskUseCase;

    private UUID userId;
    private UUID taskTypeId;
    private Task task;

    @BeforeEach
    void setUp() {
        createTaskUseCase = new CreateTaskUseCase(taskRepository);
        
        userId = UUID.randomUUID();
        taskTypeId = UUID.randomUUID();

        this.task = Task.create(userId, "Title", "Content", taskTypeId);
    }

    @Test
    void shouldCallRepositorySaveWithCorrectTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        CreateTaskUseCase.Request request = new CreateTaskUseCase.Request(
                userId, "Title", "Content", taskTypeId);
        
        createTaskUseCase.execute(request);

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldReturnResponseWithCreatedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        CreateTaskUseCase.Request request = new CreateTaskUseCase.Request(
                userId, "Title", "Content", taskTypeId);
        
        CreateTaskUseCase.Response response = createTaskUseCase.execute(request);

        assertNotNull(response);
        assertEquals(task, response.task());
    }

    @Test
    void shouldCreateTaskWithCorrectParameters() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        CreateTaskUseCase.Request request = new CreateTaskUseCase.Request(
                userId, "Test Title", "Test Content", taskTypeId);
        
        createTaskUseCase.execute(request);

        verify(taskRepository).save(argThat(savedTask -> 
            savedTask.getUserId().equals(userId) &&
            savedTask.getTitle().equals("Test Title") &&
            savedTask.getContent().equals("Test Content")
        ));
    }
}