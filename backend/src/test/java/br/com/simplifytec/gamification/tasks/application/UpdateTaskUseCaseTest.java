package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.TaskNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private UpdateTaskUseCase updateTaskUseCase;

    private UUID userId;
    private UUID taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        updateTaskUseCase = new UpdateTaskUseCase(taskRepository);
        
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        this.task = Task.create(userId, "Title", "Content", UUID.randomUUID());
    }

    @Test
    void shouldCallRepositoryFindByUserIdAndTaskIdWithCorrectParameters() {
        when(taskRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        UpdateTaskUseCase.Request request = new UpdateTaskUseCase.Request(
                userId, taskId, "New Title",  "New Content");
        
        updateTaskUseCase.execute(request);

        verify(taskRepository).findByUserIdAndTaskId(userId, taskId);
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenTaskNotFound() {
        when(taskRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.empty());

        UpdateTaskUseCase.Request request = new UpdateTaskUseCase.Request(
                userId, taskId, "New Title", "New Content");

        assertThrows(TaskNotFoundException.class, () -> updateTaskUseCase.execute(request));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldReturnResponseWithUpdatedTask() {
        when(taskRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        UpdateTaskUseCase.Request request = new UpdateTaskUseCase.Request(
                userId, taskId, "New Title", "New Content");
        
        UpdateTaskUseCase.Response response = updateTaskUseCase.execute(request);

        assertNotNull(response);
        assertEquals(task, response.task());
    }
}