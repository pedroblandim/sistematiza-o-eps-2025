package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.TaskNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;

import java.util.UUID;

public class SubmitTaskForApprovalUseCase {
    private final TaskRepository repository;

    public SubmitTaskForApprovalUseCase(TaskRepository repo) {
        this.repository = repo;
    }

    public void execute(Request request) {
        Task task = repository.findByUserIdAndTaskId(request.userId(), request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId().toString()));

        task.submit();
    }

    public record Request(UUID userId, UUID taskId) {}
}
