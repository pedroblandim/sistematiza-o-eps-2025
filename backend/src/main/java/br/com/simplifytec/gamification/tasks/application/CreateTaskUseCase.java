package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;

import java.util.UUID;

public class CreateTaskUseCase {
    private final TaskRepository repository;

    public CreateTaskUseCase(TaskRepository repo) {
        this.repository = repo;
    }

    public Response execute(Request request) {
        Task task = Task.create(request.userId(), request.title(), request.content(), request.taskTypeId());
        return new Response(repository.save(task));
    }

    public record Request(UUID userId, String title, String content, UUID taskTypeId) {}
    public record Response(Task task) {}
}
