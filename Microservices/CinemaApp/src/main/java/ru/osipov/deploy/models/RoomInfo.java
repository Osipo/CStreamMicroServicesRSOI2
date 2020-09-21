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


    public RoomInfo(Long rid, Long cid, String category, Integer size, Integer roomNum){
        this.rid = rid;
        this.roomNum = roomNum;
        this.cid = cid;
        this.category = category;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfo ri = (RoomInfo) o;
        return  Objects.equal(rid,ri.rid) && Objects.equal(cid,ri.cid) && Objects.equal(category, ri.category) &&
                Objects.equal(size,ri.size) &&
                Objects.equal(roomNum,ri.roomNum);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rid,cid,category,size,roomNum);
    }
}
