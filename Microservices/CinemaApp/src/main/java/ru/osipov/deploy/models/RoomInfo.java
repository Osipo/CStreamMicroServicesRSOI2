package ru.osipov.deploy.models;


import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString
public class RoomInfo {

    @Getter
    private final Long rid;

    @Getter
    private final String category;

    @Getter
    private final Integer seats;

    @Getter
    private final Long cid;

    public RoomInfo(Long rid, Long cid, String category, Integer seats){
        this.rid = rid;
        this.cid = cid;
        this.category = category;
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfo ri = (RoomInfo) o;
        return  Objects.equal(rid,ri.rid) && Objects.equal(cid,ri.cid) && Objects.equal(category, ri.category) &&
                 Objects.equal(seats, ri.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rid,cid,category,seats);
    }
}
