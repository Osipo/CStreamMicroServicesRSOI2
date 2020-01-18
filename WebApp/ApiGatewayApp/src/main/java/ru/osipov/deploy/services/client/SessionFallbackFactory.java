package ru.osipov.deploy.services.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionFallbackFactory implements FallbackFactory<SessionClient> {
    @Override
    public SessionClient create(Throwable cause) {
        return new SessionFeignClientFallback(cause);
    }
}
