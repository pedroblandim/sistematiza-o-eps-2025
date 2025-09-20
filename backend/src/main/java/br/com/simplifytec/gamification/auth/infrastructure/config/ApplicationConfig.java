package br.com.simplifytec.gamification.auth.infrastructure.config;

import br.com.simplifytec.gamification.auth.application.LoginUseCase;
import br.com.simplifytec.gamification.auth.infrastructure.config.service.FakeJWTService;
import br.com.simplifytec.gamification.auth.service.JWTService;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.UnaryOperator;

@Configuration
public class ApplicationConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository, UnaryOperator<String> hashFunction, JWTService jwtService) {
        return new LoginUseCase(userRepository, hashFunction, jwtService);
    }

    @Bean
    public JWTService jwtService() {
        return new FakeJWTService();
    }

    // for testing
    @Bean
    public UnaryOperator<String> hashFunction() {
        return password -> password;
    }
}