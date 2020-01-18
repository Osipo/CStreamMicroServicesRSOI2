package ru.osipov.deploy.services.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.osipov.deploy.errors.feign.*;
import ru.osipov.deploy.models.ErrorResponse;

import java.io.IOException;

public class FeignErrorDecoder implements ErrorDecoder {
    private static final Logger logger = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = "Undefined Error";
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        try {
            message = new String(response.body().asInputStream().readAllBytes());
            errorResponse = new ObjectMapper().readValue(message, ErrorResponse.class);
            logger.error("Status code " + response.status() + ", methodKey = " + methodKey);
            logger.error("Message: " + errorResponse.getMessage());
        }
        catch (IOException ignored) {}

        switch(response.status()) {
            case 400:
                return new ClientBadResponseExceptionWrapper(errorResponse.getMessage());
            case 404:
                return new ClientNotFoundExceptionWrapper(errorResponse.getMessage());
            case 401:
                return new ClientAuthenticationExceptionWrapper(errorResponse.getMessage());
            default:
                if (response.status() > 400 && response.status() < 500)
                    throw new CustomClientExceptionWrapper(errorResponse.getMessage());
                else
                    return new OtherServiceExceptionWrapper(errorResponse.getMessage());
        }
    }
}
