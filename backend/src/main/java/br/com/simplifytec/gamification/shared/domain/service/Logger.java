package br.com.simplifytec.gamification.shared.domain.service;

public interface Logger {

    void info(String message);
    void info(String message, Object... args);

    void warn(String message);
    void warn(String message, Object... args);

    void error(String message);
    void error(String message, Throwable throwable);
    void error(String message, Throwable throwable, Object... args);

    void debug(String message);
    void debug(String message, Object... args);

}