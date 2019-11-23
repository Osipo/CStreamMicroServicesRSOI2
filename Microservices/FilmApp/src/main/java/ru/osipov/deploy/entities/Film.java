package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;


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

    @Column(name = "gid", nullable = false)
    private Long gid;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film f = (Film) o;
        return Objects.equal(fid, f.fid) && Objects.equal(fname, f.fname) &&
                Objects.equal(rating, f.rating);
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
                .toString();
    }
}
