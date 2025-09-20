package br.com.simplifytec.gamification.auth.infrastructure.config.service;

import br.com.simplifytec.gamification.auth.service.JWTService;

import java.util.Base64;
import java.util.UUID;
import java.util.regex.Pattern;

// make this service minimally functional
public class FakeJWTService implements JWTService {

    private static final String SECRET = "simple-secret-key";

    public boolean verify(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            return false;
        }

        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        try {
            String payload = parts[1];
            String signature = parts[2];

            String expectedSignature = Base64.getEncoder().encodeToString(SECRET.getBytes());

            if (!signature.equals(expectedSignature)) {
                return false;
            }

            String decodedPayload = new String(Base64.getDecoder().decode(payload));
            return decodedPayload.contains("userId");
        } catch (Exception e) {
            return false;
        }
    }

    public String encode(UUID userId) {
        String header = Base64.getEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getEncoder().encodeToString(("{\"userId\":\"" + userId + "\"}").getBytes());
        String signature = Base64.getEncoder().encodeToString(SECRET.getBytes());

        return header + "." + payload + "." + signature;
    }

    @Override
    public UUID extractUserId(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }

        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        try {
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            
            // Extract userId from JSON payload
            String userIdPattern = "\"userId\":\"([^\"]+)\"";
            Pattern pattern = Pattern.compile(userIdPattern);
            java.util.regex.Matcher matcher = pattern.matcher(payload);
            
            if (matcher.find()) {
                String userIdString = matcher.group(1);
                return UUID.fromString(userIdString);
            } else {
                throw new IllegalArgumentException("userId not found in JWT token payload");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract userId from JWT token: " + e.getMessage());
        }
    }

}
