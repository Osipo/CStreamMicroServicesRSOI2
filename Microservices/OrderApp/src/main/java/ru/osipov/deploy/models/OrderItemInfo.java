package ru.osipov.deploy.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.osipov.deploy.entities.OrderItem;

@Accessors(chain = true)
@ToString
public class OrderItemInfo {

    @Getter
    private final Long id;
    @Getter
    private final Double price;
    @Getter
    private final Double discount;

    @Getter
    private final Long seanceId;
    @Getter
    private final Long seatId;

    public OrderItemInfo(Long id, Double price, Double discount, Long seanceId, Long seatId){
        this.id = id;
        this.price = price;
        this.discount = discount;
        this.seanceId = seanceId;
        this.seatId = seatId;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(id,seanceId,seatId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemInfo oi = (OrderItemInfo) o;
        return Objects.equal(id, oi.id)  && Objects.equal(price,oi.price) &&
                Objects.equal(discount,oi.discount) &&
                Objects.equal(seanceId,oi.seanceId) &&
                Objects.equal(seatId,oi.seatId);
    }

}
