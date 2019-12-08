package ru.osipov.deploy.models;


import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@ToString
@Accessors(chain = true)
public class FilmInfo {

    @Getter
    private final Long id;

    @Getter
    private final String name;

    @Getter
    private final Short rating;

    @Getter
    private final Long gid;

    public FilmInfo(Long id, String name, Short rating, Long gid){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.gid = gid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmInfo f = (FilmInfo) o;
        return Objects.equal(id,f.id) && Objects.equal(name, f.name) &&
               Objects.equal(rating, f.rating) &&
               Objects.equal(gid,f.gid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,name, rating,gid);
    }
}