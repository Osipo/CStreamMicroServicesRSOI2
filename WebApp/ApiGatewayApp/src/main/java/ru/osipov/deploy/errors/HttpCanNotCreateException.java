package ru.osipov.deploy.errors;

public class HttpCanNotCreateException extends RuntimeException {
    public HttpCanNotCreateException(String message) {
        super(message);
    }
}
