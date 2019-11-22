package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(chain = true)
@ToString
public class SeanceDescription {

    @Getter
    private final String cinema;

    @Getter
    private final String location;
    @Getter
    private final String film;

    @Getter
    private final String genre;
    @Getter
    private final Short rating;
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    public SeanceDescription() {
        this("Error","","","",(short)-1,LocalDate.now());
    }
    public SeanceDescription(String cinema, String loc, String film, String genre, Short rating, LocalDate date){
        this.cinema = cinema;
        this.location = loc;
        this.film = film;
        this.genre = genre;
        this.rating = rating;
        this.date = date;
    }
}
