package br.com.simplifytec.gamification.auth.infrastructure.config.service;

import br.com.simplifytec.gamification.auth.domain.exception.UnauthorizedException;
import br.com.simplifytec.gamification.auth.service.JWTService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Auth0JWTService implements JWTService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationHours;

    public Auth0JWTService(@Value("${jwt.secret:default-secret-key}") String secret,
                          @Value("${jwt.expiration.hours:24}") long expirationHours) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expirationHours = expirationHours;
    }

    @Override
    public String encode(UUID userId, Map<String, Object> payload) {
        var builder = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)));

        builder.withSubject(userId.toString());

        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) continue;

            switch (value) {
                case String s -> builder.withClaim(key, s);
                case Integer i -> builder.withClaim(key, i);
                case Long l -> builder.withClaim(key, l);
                case Boolean b -> builder.withClaim(key, b);
                case Date date -> builder.withClaim(key, date);
                default -> builder.withClaim(key, value.toString());
            }
        }

        return builder.sign(algorithm);
    }

    @Override
    public boolean verify(String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            return false;
        }

        try {
            verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> extractPayload(String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            throw new UnauthorizedException("JWT token cannot be null or empty");
        }

        try {
            DecodedJWT decodedJWT = verifier.verify(jwt);
            Map<String, Object> payload = new HashMap<>();

            decodedJWT.getClaims().forEach((key, claim) -> {
                if (claim.isNull()) {
                    payload.put(key, null);
                } else if (claim.asString() != null) {
                    payload.put(key, claim.asString());
                } else if (claim.asInt() != null) {
                    payload.put(key, claim.asInt());
                } else if (claim.asLong() != null) {
                    payload.put(key, claim.asLong());
                } else if (claim.asBoolean() != null) {
                    payload.put(key, claim.asBoolean());
                } else if (claim.asDate() != null) {
                    payload.put(key, claim.asDate());
                } else {
                    payload.put(key, claim.as(Object.class));
                }
            });

            return payload;
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException("Invalid JWT token: " + e.getMessage());
        }
    }

    @Override
    public UUID extractUserId(String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            throw new UnauthorizedException("JWT token cannot be null or empty");
        }

        try {
            DecodedJWT decodedJWT = verifier.verify(jwt);
            String subject = decodedJWT.getSubject();

            if (subject == null) {
                throw new UnauthorizedException("userId (subject) not found in JWT token");
            }

            return UUID.fromString(subject);
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException("Invalid JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid userId format in JWT token");
        }
    }
}