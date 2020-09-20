package ru.osipov.deploy.models;


import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@ToString
public class RoomInfo {

    @Getter
    private final Long rid;

    @Getter
    private final String category;

    @Getter
    private final Integer size;

    @Getter
    private final Long cid;

    @Getter
    private final Integer roomNum;

    @Getter
    private final List<SeatInfo> seats;


    public RoomInfo(Long rid, Long cid, String category, Integer size, Integer roomNum,List<SeatInfo> seats){
        this.rid = rid;
        this.roomNum = roomNum;
        this.cid = cid;
        this.category = category;
        this.size = size;
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfo ri = (RoomInfo) o;
        return  Objects.equal(rid,ri.rid) && Objects.equal(cid,ri.cid) && Objects.equal(category, ri.category) &&
                Objects.equal(size,ri.size) &&
                Objects.equal(roomNum,ri.roomNum) &&
                 Objects.equal(seats, ri.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rid,cid,category,size,roomNum,seats);
    }
}
