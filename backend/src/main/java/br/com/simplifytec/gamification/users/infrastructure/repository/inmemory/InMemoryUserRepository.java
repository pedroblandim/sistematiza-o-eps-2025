package br.com.simplifytec.gamification.users.infrastructure.repository.inmemory;

import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@Profile("inmemory")
public class InMemoryUserRepository implements UserRepository {

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(store.get(userId));
    }    @Override

    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    @Override
    public Task save(User task) {
        return null;
    }

    private final ConcurrentMap<UUID, User> store = new ConcurrentHashMap<>();
}