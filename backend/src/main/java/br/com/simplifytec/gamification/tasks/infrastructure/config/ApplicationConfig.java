package br.com.simplifytec.gamification.tasks.infrastructure.config;

import br.com.simplifytec.gamification.tasks.application.*;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreateTaskUseCase createTaskEntryUseCase(TaskRepository repository) {
        return new CreateTaskUseCase(repository);
    }

    @Bean
    public UpdateTaskUseCase updateTaskEntryUseCase(TaskRepository repository) {
        return new UpdateTaskUseCase(repository);
    }

    @Bean
    public ListTasksUseCase listTasksEntryUseCase(TaskRepository repository) {
        return new ListTasksUseCase(repository);
    }

    @Bean
    public ApproveTaskUseCase approveTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        return new ApproveTaskUseCase(taskRepository, userRepository);
    }

    @Bean
    public ListApprovedTasksUseCase listApprovedTasksUseCase(TaskRepository repository) {
        return new ListApprovedTasksUseCase(repository);
    }

    @Bean
    public ListUserTasksUseCase listUserTasksUseCase(TaskRepository repository) {
        return new ListUserTasksUseCase(repository);
    }

    @Bean
    public SubmitTaskForApprovalUseCase submitTaskForApprovalUseCase(TaskRepository taskRepository) {
        return new SubmitTaskForApprovalUseCase(taskRepository);
    }
}
