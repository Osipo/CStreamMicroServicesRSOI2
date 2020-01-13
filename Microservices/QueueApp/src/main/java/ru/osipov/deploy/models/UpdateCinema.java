package ru.osipov.deploy.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCinema {

    @NonNull
    private Long cid;

    @NotNull
    @NotBlank
    @Size(min = 3)
    @Expose
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    @Expose
    private String country;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    @Expose
    private String city;


    @Expose
    private String region;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    @Expose
    private String street;


    private CreateSeance[] seances;
}
