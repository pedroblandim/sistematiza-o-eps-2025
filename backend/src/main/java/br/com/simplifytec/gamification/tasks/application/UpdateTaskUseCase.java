package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.ForbiddenException;
import br.com.simplifytec.gamification.tasks.domain.exception.TaskNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.exception.UserNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;

import java.util.UUID;

public class UpdateTaskUseCase {
    private final TaskRepository repository;
    private final UserRepository userRepository;

    public UpdateTaskUseCase(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Response execute(Request request) {
        validateUser(request.userId());

        // here the authorization is managed by the query using userId as a filter
        Task task = repository.findByUserIdAndTaskId(request.userId(), request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId().toString()));

        task.updateTitle(request.newTitle());
        task.updateContent(request.newContent());

        return new Response(repository.update(task));
    }

    private void validateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (user.isAdmin()) {
            throw new ForbiddenException("Admin users cannot update tasks");
        }
    }

    public record Request(UUID userId, UUID taskId, String newTitle, String newContent) {}
    public record Response(Task task) {}
}
