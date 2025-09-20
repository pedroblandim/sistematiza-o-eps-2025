package br.com.simplifytec.gamification.auth.infrastructure.web;

import br.com.simplifytec.gamification.auth.application.LoginUseCase;
import br.com.simplifytec.gamification.auth.domain.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    // TODO: implement refresh token
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = loginUseCase.execute(
                    new LoginUseCase.Request(request.email(), request.password())
            );

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null));
        }
    }

    public record LoginRequest(String email, String password) {}
    public record LoginResponse(String token) {}
}