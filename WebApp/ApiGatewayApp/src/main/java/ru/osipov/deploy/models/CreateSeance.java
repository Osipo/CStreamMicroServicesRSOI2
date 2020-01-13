package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeance {

    @Nonnull
    @Min(-1)
    private  Long cid;

    @Nonnull
    @Min(-1)
    private  Long fid;

    @Nonnull
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;
}
