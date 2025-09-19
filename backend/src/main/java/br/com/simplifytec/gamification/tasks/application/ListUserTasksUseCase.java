package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;

import java.util.List;
import java.util.UUID;

public class ListUserTasksUseCase {
    private final TaskRepository repository;

    public ListUserTasksUseCase(TaskRepository repo) {
        this.repository = repo;
    }

    public Response execute(Request request) {
        return new Response(repository.listByUserId(request.userId()));
    }

    public record Request(UUID userId) {}
    public record Response(List<Task> tasks) {}
}
