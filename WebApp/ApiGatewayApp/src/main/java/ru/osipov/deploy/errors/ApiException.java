package ru.osipov.deploy.errors;

import lombok.Getter;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public class ApiException extends RuntimeException {

    @Getter
    private  int code;

    @Getter
    private Map<String, List<String>> headers;

    @Getter
    private String reqBody;

    @Getter
    private String responseBody;

    @Getter
    private String path;

    public ApiException(String message, Throwable throwable, int code, Map<String, List<String>> respHeaders,
                        String respBody, String URI, String requestBody) {
        super(message, throwable);
        this.code = code;
        this.headers = respHeaders;
        this.responseBody = respBody;
        this.path = URI;
        this.reqBody = requestBody;
    }
}
