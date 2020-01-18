package ru.osipov.deploy.errors.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class ClientNotFoundExceptionWrapper extends HystrixBadRequestException {
    public ClientNotFoundExceptionWrapper(String message) {
        super(message);
    }
}
