package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateFilm {

    @Nonnull
    @NotEmpty
    @NotBlank
    private String name;

    @Nonnull
    @Min(0)
    @Max(100)
    private Short rating;

    @Nonnull
    @Min(-1)
    private Long gid;
}
