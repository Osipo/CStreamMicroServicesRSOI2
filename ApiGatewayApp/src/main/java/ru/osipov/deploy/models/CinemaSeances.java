package ru.osipov.deploy.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class CinemaSeances {
    @Expose
    private CinemaInfo cinema;
    @Expose
    private SeanceInfo[] seances;
}
