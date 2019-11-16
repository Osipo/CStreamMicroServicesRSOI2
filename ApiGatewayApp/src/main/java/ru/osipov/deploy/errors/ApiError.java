package ru.osipov.deploy.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String reason;
    private String path;
    private Integer code;
    private String ex;
}
