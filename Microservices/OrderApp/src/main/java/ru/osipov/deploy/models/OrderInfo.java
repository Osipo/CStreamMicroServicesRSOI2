package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.osipov.deploy.entities.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Accessors(chain = true)
@ToString
public class OrderInfo {

    @Getter
    private final Long oid;

    @Getter
    private final Long uid;

    @Getter
    private final Double sum;

    @Getter
    private final String status;
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate created;
    @Getter
    @JsonFormat(pattern = "HH:mm:ss.SS")
    private final LocalTime ctime;
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate updated;

    @Getter
    private final List<OrderItemInfo> items;

    public OrderInfo(Long id, Long uid, Double sum, String status, LocalDate created, LocalTime time, LocalDate updated, List<OrderItemInfo> items){
        this.oid = id;
        this.uid = uid;
        this.status = status;
        this.sum = sum;
        this.created = created;
        this.updated = updated;
        this.ctime = time;
        this.items = items;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(oid,sum,created,ctime,uid,items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInfo oi = (OrderInfo) o;
        return Objects.equal(oid, oi.oid) && Objects.equal(sum,oi.sum) &&
                Objects.equal(uid,oi.uid) && Objects.equal(status, oi.status) &&
                Objects.equal(created,oi.created) &&
                Objects.equal(updated,oi.updated) &&
                Objects.equal(ctime,oi.ctime) && Objects.equal(items, oi.items);
    }
}
