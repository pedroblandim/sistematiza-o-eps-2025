package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;

import java.util.List;

public class ListApprovedTasksUseCase {
    private final TaskRepository repository;

    public ListApprovedTasksUseCase(TaskRepository repo) {
        this.repository = repo;
    }

    public Response execute() {
        return new Response(repository.list(List.of(TaskStatus.APPROVED)));
    }

    public record Response(List<Task> tasks) {}
}
