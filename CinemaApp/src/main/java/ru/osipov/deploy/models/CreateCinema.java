package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateCinema {

    @NonNull
    @NotBlank
    @Size(min = 3)
    private String name;

    @NonNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    private String country;

    @NonNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    private String city;


    private String region;

    @NonNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    private String street;
}
