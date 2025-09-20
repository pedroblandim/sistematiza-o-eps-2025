package br.com.simplifytec.gamification.auth.service;

import java.util.Map;
import java.util.UUID;

public interface JWTService {

    String encode(UUID userId, Map<String, Object> payload);
    boolean verify(String jwt);
    Map<String, Object> extractPayload(String jwt);
    UUID extractUserId(String jwt);

}
