package ru.osipov.deploy.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@ToString
@Accessors(chain = true)
public class FilmGenre {
    @Getter
    private final Long id;

    @Getter
    private final String name;

    @Getter
    private final Short rating;

    @Getter
    private final GenreInfo genre;

    public FilmGenre(Long id, String name, Short rating, GenreInfo genre){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmGenre f = (FilmGenre) o;
        return Objects.equal(id,f.id) && Objects.equal(name, f.name) &&
                Objects.equal(rating, f.rating) &&
                Objects.equal(genre,f.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,name, rating,genre);
    }
}
