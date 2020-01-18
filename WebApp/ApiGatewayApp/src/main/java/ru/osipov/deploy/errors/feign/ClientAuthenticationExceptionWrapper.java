package ru.osipov.deploy.errors.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.security.core.AuthenticationException;

public class ClientAuthenticationExceptionWrapper extends HystrixBadRequestException {
    public ClientAuthenticationExceptionWrapper(String message) {
        super(message);
    }

    public ClientAuthenticationExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }
}
