package br.com.simplifytec.gamification.tasks.domain.repository;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);
    Task update(Task task);
    Optional<Task> findById(UUID taskId);
    Optional<Task> findByUserIdAndTaskId(UUID userId, UUID taskId);
    List<Task> list();
    List<Task> list(List<TaskStatus> statuses);
    List<Task> listByUserId(UUID userId);
}
