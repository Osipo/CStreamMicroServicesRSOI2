package ru.osipov.deploy.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString
public class SeatInfo {

    @Getter
    private final Long sid;

    @Getter
    private final Long num;

    @Getter
    private final String state;

    @Getter
    private final Long rid;

    public SeatInfo(Long sid, Long num, String state, Long rid){
        this.sid = sid;
        this.num = num;
        this.state = state;
        this.rid = rid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatInfo si = (SeatInfo) o;
        return  Objects.equal(sid,si.sid) && Objects.equal(rid,si.rid) && Objects.equal(num, si.num) &&
                Objects.equal(state,si.state);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sid,num,state,rid);
    }
}
