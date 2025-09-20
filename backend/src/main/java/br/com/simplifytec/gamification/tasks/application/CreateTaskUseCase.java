package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.ForbiddenException;
import br.com.simplifytec.gamification.tasks.domain.exception.UserNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;

import java.util.UUID;

public class CreateTaskUseCase {
    private final TaskRepository repository;
    private final UserRepository userRepository;

    public CreateTaskUseCase(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Response execute(Request request) {
        validateUser(request.userId());

        Task task = Task.create(request.userId(), request.title(), request.content(), request.taskTypeId());
        return new Response(repository.save(task));
    }

    private void validateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (user.isAdmin()) {
            throw new ForbiddenException("Admin users cannot create tasks");
        }
    }

    public record Request(UUID userId, String title, String content, UUID taskTypeId) {}
    public record Response(Task task) {}
}
