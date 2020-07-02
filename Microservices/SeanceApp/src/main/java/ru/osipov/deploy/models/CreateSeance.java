package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @Min(-1)
    private Long rid;

    @Nonnull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @Nonnull
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime time;
}
