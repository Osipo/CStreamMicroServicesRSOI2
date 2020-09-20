package ru.osipov.deploy.models;


import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString
public class TicketInfo {

    @Getter
    private long seanceId;
    @Getter
    private long fid;
    @Getter
    private long cid;
    @Getter
    private long rid;
    @Getter
    private long seatId;
    @Getter
    private double price;
    @Getter
    private String ptype;

    public TicketInfo(long seanceId, long fid, long cid, long rid, long seatId, double price, String ptype){
        this.seanceId = seanceId;
        this.fid = fid;
        this.cid = cid;
        this.rid = rid;
        this.seatId = seatId;
        this.price = price;
        this.ptype = ptype;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketInfo ti = (TicketInfo) o;
        return  Objects.equal(seanceId,ti.seanceId) && Objects.equal(seatId,ti.seatId) &&
                Objects.equal(price,ti.price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(seanceId,seatId,price);
    }
}
