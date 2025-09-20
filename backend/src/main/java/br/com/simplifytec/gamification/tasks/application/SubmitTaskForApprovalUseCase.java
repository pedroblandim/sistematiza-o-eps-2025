package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.ForbiddenException;
import br.com.simplifytec.gamification.tasks.domain.exception.TaskNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.exception.UserNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;

import java.util.UUID;

public class SubmitTaskForApprovalUseCase {
    private final TaskRepository repository;
    private final UserRepository userRepository;

    public SubmitTaskForApprovalUseCase(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void execute(Request request) {
        validateUser(request.userId());

        Task task = repository.findByUserIdAndTaskId(request.userId(), request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId().toString()));

        task.submit();
    }

    private void validateUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if (user.isAdmin()) {
            throw new ForbiddenException("Admin users cannot submit tasks for approval");
        }
    }

    public record Request(UUID userId, UUID taskId) {}
}
