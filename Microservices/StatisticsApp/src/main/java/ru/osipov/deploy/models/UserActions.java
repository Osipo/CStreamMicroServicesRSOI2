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
public class UserActions {
    
    @Getter
    private final UUID id;
    @Getter
    private final Long userId;
    @Getter
    private final String username;
    @Getter
    private final String action;
    @Getter
    private final String path;
    @Getter
    private final String responseCode;
    @Getter
    private final LocalDate date;
    
    public UserActions(UUID uuid, Long uid,String username,String action, String path, String responseCode){
        this.id = uuid;
        this.uid = uid;
        this.username = username;
        this.action = action;
        this.path = path;
        this.responseCode = responseCode;
        this.date = LocalDate.now();
    }
    
}