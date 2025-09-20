package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.shared.domain.service.Logger;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;

import java.util.*;

public class ListTasksUseCase {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Logger logger;

    public ListTasksUseCase(TaskRepository taskRepository, UserRepository userRepository, Logger logger) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.logger = logger;
    }

    public Response execute(Request request) {

        Optional<User> user = userRepository.findById(request.userId());

        List<Task> tasks = new ArrayList<>();

        if (user.isEmpty()) {
            return buildResponse(tasks);
        }

        if (user.get().isAdmin()) {
            return buildResponse(taskRepository.list(request.statuses()));
        }

        return buildResponse(taskRepository.list(request.userId(), request.statuses()));
    }

    // TODO: find a better way to handle this
    private Response buildResponse(List<Task> tasks) {
        List<TaskResponse> tasksResponses = tasks.stream().map(task -> {
            Optional<User> user = userRepository.findById(task.getUserId());

            if (user.isEmpty()) {
                logger.error("User not found " + task.getUserId());
                return null;
            }

            return new TaskResponse(task, user.get().getEmail());
        }).filter(Objects::nonNull).toList();

        return new Response(tasksResponses);
    }

    public record Request(UUID userId, List<TaskStatus> statuses) {}
    public record Response(List<TaskResponse> tasks) {}

    // TODO: find a better way to handle this
    public record TaskResponse(Task task, String userEmail) {}
}
