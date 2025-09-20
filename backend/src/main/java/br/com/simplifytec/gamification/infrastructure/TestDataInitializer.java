package br.com.simplifytec.gamification.infrastructure;

import br.com.simplifytec.gamification.shared.domain.service.Logger;
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
    private final Logger logger;

    public TestDataInitializer(UserRepository userRepository, Logger logger) {
        this.userRepository = userRepository;
        this.logger = logger;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting test data initialization...");
        createTestUsers();
        logger.info("Test data initialization completed");
    }

    private void createTestUsers() {
        Date now = new Date();

        User adminUser = createUser(
                UUID.fromString("730049f6-8567-4ba9-ad14-f3ab7488c8b9"),
                "admin@test.com",
                "admin123",
                true,
                now
        );

        User normalUser = createUser(
                UUID.fromString("0dbf1f2d-aba3-4f43-b309-a99e5c882a68"),
                "user@test.com",
                "user123",
                false,
                now
        );

        userRepository.save(adminUser);
        userRepository.save(normalUser);

        logger.info("Test users created successfully:");
        logger.info("  Admin: admin@test.com / admin123");
        logger.info("  User: user@test.com / user123");
    }

    private User createUser(UUID id, String email, String password, boolean isAdmin, Date date) {
        return new User(id, email, password, isAdmin, date, date);
    }
}