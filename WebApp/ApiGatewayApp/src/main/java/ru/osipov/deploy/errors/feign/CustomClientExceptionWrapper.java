package ru.osipov.deploy.errors.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class CustomClientExceptionWrapper extends HystrixBadRequestException {
    public CustomClientExceptionWrapper(String message) {
        super(message);
    }
}
