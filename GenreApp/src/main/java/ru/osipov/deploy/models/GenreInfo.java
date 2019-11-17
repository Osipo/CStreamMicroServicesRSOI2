package ru.osipov.deploy.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Accessors(chain = true)
public class GenreInfo {

    @Getter
    private final Long id;

    @Getter
    private final String name;


    @Getter
    private final String remarks;


    public GenreInfo(Long id, String n, String r){
        this.id = id;
        this.name = n;
        this.remarks = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreInfo g = (GenreInfo) o;
        return Objects.equal(id,g.id) && Objects.equal(name, g.name) &&
                (remarks == null) ? g.remarks == null : Objects.equal(remarks, g.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,name, remarks != null ? remarks : 0);
    }
}