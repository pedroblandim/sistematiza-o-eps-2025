package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.ForbiddenException;
import br.com.simplifytec.gamification.tasks.domain.exception.UserNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class ListUserTasksUseCase {
    private final TaskRepository repository;
    private final UserRepository userRepository;

    public ListUserTasksUseCase(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Response execute(Request request) {
        validateUser(request.userId());

        return new Response(repository.listByUserId(request.userId()));
    }

    private void validateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (user.isAdmin()) {
            throw new ForbiddenException("Admin users cannot list user tasks");
        }
    }

    public record Request(UUID userId) {}
    public record Response(List<Task> tasks) {}
}
