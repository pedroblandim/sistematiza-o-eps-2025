package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;

import java.util.List;

public class ListTasksUseCase {
    private final TaskRepository repository;

    public ListTasksUseCase(TaskRepository repo) {
        this.repository = repo;
    }

    public Response execute() {
        return new Response(repository.list());
    }

    public record Response(List<Task> tasks) {}
}
