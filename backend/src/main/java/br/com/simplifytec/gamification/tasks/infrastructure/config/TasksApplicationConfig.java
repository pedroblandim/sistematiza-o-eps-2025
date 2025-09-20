package br.com.simplifytec.gamification.tasks.infrastructure.config;

import br.com.simplifytec.gamification.shared.domain.service.Logger;
import br.com.simplifytec.gamification.tasks.application.*;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TasksApplicationConfig {

    @Bean
    public CreateTaskUseCase createTaskEntryUseCase(TaskRepository repository, UserRepository userRepository) {
        return new CreateTaskUseCase(repository, userRepository);
    }

    @Bean
    public UpdateTaskUseCase updateTaskEntryUseCase(TaskRepository repository, UserRepository userRepository) {
        return new UpdateTaskUseCase(repository, userRepository);
    }

    @Bean
    public ListTasksUseCase listTasksEntryUseCase(TaskRepository repository, UserRepository userRepository, Logger logger) {
        return new ListTasksUseCase(repository, userRepository, logger);
    }

    @Bean
    public ApproveTaskUseCase approveTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        return new ApproveTaskUseCase(taskRepository, userRepository);
    }

    @Bean
    public RejectTaskUseCase rejectTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        return new RejectTaskUseCase(taskRepository, userRepository);
    }

    @Bean
    public SubmitTaskForApprovalUseCase submitTaskForApprovalUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        return new SubmitTaskForApprovalUseCase(taskRepository, userRepository);
    }
}
