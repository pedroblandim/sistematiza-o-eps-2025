package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.TaskNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;

import java.util.UUID;

public class UpdateTaskUseCase {
    private final TaskRepository repository;

    public UpdateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public Response execute(Request request) {
        // here the authorization is managed by the query using userId as a filter
        Task task = repository.findByUserIdAndTaskId(request.userId(), request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId().toString()));

        task.updateTitle(request.newTitle());
        task.updateContent(request.newContent());

        return new Response(repository.update(task));
    }

    public record Request(UUID userId, UUID taskId, String newTitle, String newContent) {}
    public record Response(Task task) {}
}
