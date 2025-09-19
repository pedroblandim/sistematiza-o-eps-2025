package br.com.simplifytec.gamification.users.domain.repository;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.users.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Task save(User task);
    Optional<User> findById(UUID userId);
}
