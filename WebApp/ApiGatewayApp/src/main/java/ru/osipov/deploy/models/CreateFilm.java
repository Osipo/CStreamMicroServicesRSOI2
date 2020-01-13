package ru.osipov.deploy.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateFilm {

    @NotNull
    @NotEmpty
    @NotBlank
    @Pattern(regexp = "^[A-Za-z\\s]+$")
    @Expose
    private String name;

    @NotNull
    @Min(0)
    @Max(100)
    @Expose
    private Short rating;

    @NotNull
    @Min(-1)
    @Expose
    private Long gid;
}
