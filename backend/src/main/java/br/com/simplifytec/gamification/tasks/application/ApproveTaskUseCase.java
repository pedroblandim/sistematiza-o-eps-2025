package br.com.simplifytec.gamification.tasks.application;

import br.com.simplifytec.gamification.tasks.domain.exception.ForbiddenException;
import br.com.simplifytec.gamification.tasks.domain.exception.TaskNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.exception.UserNotFoundException;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository; 

import java.util.UUID;

public class ApproveTaskUseCase {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ApproveTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public void execute(Request request) {
        validate(request);

        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new TaskNotFoundException(request.taskId().toString()));

        task.approve();
    }

    public record Request(UUID userId, UUID taskId) {}

    private void validate(Request request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId().toString()));

        if (!user.isAdmin()) {
            throw new ForbiddenException("User must be ADMIN");
        }
    }
}
