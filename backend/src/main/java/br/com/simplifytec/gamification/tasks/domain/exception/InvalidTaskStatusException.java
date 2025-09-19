package br.com.simplifytec.gamification.tasks.domain.exception;

public class InvalidTaskStatusException extends RuntimeException {
    public InvalidTaskStatusException(String message) {
        super(message);
    }
}