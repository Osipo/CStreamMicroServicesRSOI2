package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Accessors(chain = true)
@Entity
@Table(name = "film")
public class Film {
    @Id
    @Column(name= "fid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;

    @Column(name = "fname", nullable = false)
    private String fname;

    @Column(name = "rating", nullable = false)
    private Short rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "film_genres",
            joinColumns = @JoinColumn(name = "fid"),
            inverseJoinColumns = @JoinColumn(name = "gid"))
    private List<Genre> genres = new ArrayList<>();



    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film f = (Film) o;
        return Objects.equal(fid, f.fid) && Objects.equal(fname, f.fname) &&
                Objects.equal(rating, f.rating) && Objects.equal(genres, f.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fid, fname, rating);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("fname", fname)
                .add("rating", rating)
                .add("genres",genres)
                .toString();
    }
}
