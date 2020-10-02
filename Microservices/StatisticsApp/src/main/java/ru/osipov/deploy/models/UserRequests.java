package ru.osipov.deploy.models;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Accessors(chain = true)
@ToString
public class UserRequests {
    
    @Getter
    private final UUID id;
    
    @Getter
    private final Long userId;
    
    @Getter
    private final String url;
    
    @Getter
    private final String service;
    
    @Getter
    private final String controller;
    
    @Getter
    private final String method;
    
    @Getter
    private final String type;
    
    public UserRequests(UUID id, Long userId, String url, String service,
        String controller, String method, String type){
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.service = service;
        this.controller = controller;
        this.method = method;
        this.type = type;
    }
}