package ru.osipov.deploy.errors.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class OtherServiceExceptionWrapper extends HystrixBadRequestException {
    public OtherServiceExceptionWrapper(String message) {
        super(message);
    }
}
