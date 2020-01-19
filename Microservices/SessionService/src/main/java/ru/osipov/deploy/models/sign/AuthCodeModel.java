package ru.osipov.deploy.models.sign;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class AuthCodeModel {
    private UUID code;

    private UUID clientId;

    private Long userId;

    public AuthCodeModel(){

    }
}
