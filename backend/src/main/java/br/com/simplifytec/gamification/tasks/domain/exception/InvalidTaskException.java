package br.com.simplifytec.gamification.tasks.domain.exception;

public class InvalidTaskException extends RuntimeException {
    public InvalidTaskException(String message) {
        super(message);
    }
}