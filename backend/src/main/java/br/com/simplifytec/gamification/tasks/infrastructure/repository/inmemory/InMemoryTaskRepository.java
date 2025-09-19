package br.com.simplifytec.gamification.tasks.infrastructure.repository.inmemory;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;
import br.com.simplifytec.gamification.tasks.domain.repository.TaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@Profile("inmemory") // ativa com SPRING_PROFILES_ACTIVE=inmemory
public class InMemoryTaskRepository implements TaskRepository {

    @Override
    public Task save(Task task) {
        if (task.getId() == null) task.setId(UUID.randomUUID());

        store.put(task.getId(), task);

        return task;
    }

    @Override
    public Task update(Task task) {
        store.put(task.getId(), task);

        return task;
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return Optional.empty();
    }

    @Override
    public List<Task> list(List<TaskStatus> statuses) {
        var tasks = store.values().stream();

        if (statuses != null && !statuses.isEmpty()) {
            tasks = tasks.filter(task -> statuses.contains(task.getStatus()));
        }

        return tasks.toList();
    }

    @Override
    public List<Task> list() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<Task> findByUserIdAndTaskId(UUID userId, UUID taskId) {
        var task = store.get(taskId);

        if (task != null && task.getUserId().equals(userId)) {
            return Optional.of(task);
        }

        return Optional.empty();
    }

    @Override
    public List<Task> listByUserId(UUID userId) {
        var tasks = store.values().stream()
                .filter(task -> task.getUserId().equals(userId));
        
        return tasks.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private final ConcurrentMap<UUID, Task> store = new ConcurrentHashMap<>();
}