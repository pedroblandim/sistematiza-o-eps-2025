package br.com.simplifytec.gamification.config;

import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Profile("test")
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public TestDataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createTestUsers();
    }

    private void createTestUsers() {
        Date now = new Date();

        User adminUser = createUser(
                UUID.randomUUID(),
                "admin@test.com",
                "admin123",
                true,
                now
        );

        User normalUser = createUser(
                UUID.randomUUID(),
                "user@test.com",
                "user123",
                false,
                now
        );

        userRepository.save(adminUser);
        userRepository.save(normalUser);

        System.out.println("Test users created:");
        System.out.println("Admin: admin@test.com / admin123");
        System.out.println("User: user@test.com / user123");
    }

    private User createUser(UUID id, String email, String password, boolean isAdmin, Date date) {
        return new User(id, email, password, isAdmin, date, date);
    }
}