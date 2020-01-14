package ru.osipov.deploy.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateCinema {

    @NotNull
    @NotBlank
    @Size(min = 4)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9\\s]+$")
    @Expose
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-z\\s]+$")
    @Expose
    private String country;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 4)
    @Pattern(regexp = "^[A-Za-z\\s]+$")
    @Expose
    private String city;


    @Expose
    @Pattern(regexp = "^$|^[A-Za-z_\\s]+ | [A-Za-z_\\s]?$")
    private String region;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 5)
    @Pattern(regexp = "[A-Za-z\\s]+")
    @Expose
    private String street;


    @Valid
    private CreateSeance[] seances;
}
