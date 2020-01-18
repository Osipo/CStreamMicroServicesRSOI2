package ru.osipov.deploy.errors;

public class ServiceAccessException extends RuntimeException {
    public ServiceAccessException(String message) {
        super(message);
    }
}
