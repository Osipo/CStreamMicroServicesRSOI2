package ru.osipov.deploy.models;


import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString
public class TicketInfo {

    @Getter
    private long tid;
    @Getter
    private long fid;
    @Getter
    private long cid;
    @Getter
    private double price;

    public TicketInfo(long tid, long fid, long cid, double price){
        this.tid = tid;
        this.fid = fid;
        this.cid = cid;
        this.price = price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketInfo ti = (TicketInfo) o;
        return  Objects.equal(cid,ti.cid) && Objects.equal(fid,ti.fid) && Objects.equal(tid,ti.tid) &&
                Objects.equal(price,ti.price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cid,fid,tid,price);
    }
}
