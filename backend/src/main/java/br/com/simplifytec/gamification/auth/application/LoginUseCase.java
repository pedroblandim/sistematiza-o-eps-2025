package br.com.simplifytec.gamification.auth.application;

import br.com.simplifytec.gamification.auth.domain.exception.UnauthorizedException;
import br.com.simplifytec.gamification.auth.service.JWTService;
import br.com.simplifytec.gamification.users.domain.model.User;
import br.com.simplifytec.gamification.users.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class LoginUseCase {

    private final UserRepository userRepository;
    private final UnaryOperator<String> hashFunction;
    private final JWTService jwtService;

    // TODO: decide if is ok for LoginUseCase to use userRepository
    public LoginUseCase(UserRepository userRepository, UnaryOperator<String> hashFunction, JWTService jwtService) {
        this.userRepository = userRepository;
        this.hashFunction = hashFunction;
        this.jwtService = jwtService;
    }

    public String execute(Request request) throws UnauthorizedException {
        String hashedPassword = hashFunction.apply(request.password);

        Optional<User> userOpt = userRepository.findByEmail(request.email());

        if (userOpt.isEmpty()) throw new UnauthorizedException("Invalid credentials for email " + request.email());

        User user = userOpt.get();

        if (!hashedPassword.equals(user.getPassword()))
            throw new UnauthorizedException("Invalid credentials for email " + request.email());

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("isAdmin", user.isAdmin());

        return jwtService.encode(user.getId(), payload);
    }

    public record Request(String email, String password) {}

}
