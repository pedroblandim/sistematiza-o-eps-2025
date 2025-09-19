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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmitTaskForApprovalUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private SubmitTaskForApprovalUseCase submitTaskForApprovalUseCase;

    private UUID userId;
    private UUID taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        submitTaskForApprovalUseCase = new SubmitTaskForApprovalUseCase(taskRepository);
        
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        this.task = Task.create(userId, "Title", "Content", UUID.randomUUID());
    }

    @Test
    void shouldCallRepositoryFindByUserIdAndTaskIdWithCorrectParameters() {
        when(taskRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.of(task));

        SubmitTaskForApprovalUseCase.Request request = new SubmitTaskForApprovalUseCase.Request(userId, taskId);
        
        submitTaskForApprovalUseCase.execute(request);

        verify(taskRepository).findByUserIdAndTaskId(userId, taskId);
    }

    @Test
    void shouldCallTaskSubmitMethod() {
        Task spyTask = spy(task);
        when(taskRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.of(spyTask));

        SubmitTaskForApprovalUseCase.Request request = new SubmitTaskForApprovalUseCase.Request(userId, taskId);
        
        submitTaskForApprovalUseCase.execute(request);

        verify(spyTask).submit();
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenTaskNotFound() {
        when(taskRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.empty());

        SubmitTaskForApprovalUseCase.Request request = new SubmitTaskForApprovalUseCase.Request(userId, taskId);

        assertThrows(TaskNotFoundException.class, () -> submitTaskForApprovalUseCase.execute(request));
    }
}