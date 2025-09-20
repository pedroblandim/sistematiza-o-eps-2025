package br.com.simplifytec.gamification.auth.service;

import java.util.UUID;

public interface JWTService {

    String encode(UUID userId);
    boolean verify(String jwt);
    UUID extractUserId(String jwt);

}
