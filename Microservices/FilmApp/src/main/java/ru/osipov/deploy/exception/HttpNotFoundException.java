package ru.osipov.deploy.exception;

public class HttpNotFoundException extends RuntimeException {

    public HttpNotFoundException(String message) {
        super(message);
    }
}
