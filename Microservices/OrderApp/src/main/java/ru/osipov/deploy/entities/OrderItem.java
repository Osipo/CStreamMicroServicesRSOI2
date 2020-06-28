package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "order_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sid", "seat_id"})
})
public class OrderItem {
    @Id
    @Column(name= "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount",nullable = false)
    private Double discount;

    @Column(name = "sid",nullable = false)
    private Long sid;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @ManyToOne
    @JoinColumn(name="oid",nullable = false)
    private Order order;

    @Override
    public int hashCode(){
        return Objects.hashCode(id,price,discount,sid,seatId,order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem oi = (OrderItem) o;
        return Objects.equal(id, oi.id) && Objects.equal(price,oi.price) &&
                Objects.equal(discount,oi.discount) &&
                Objects.equal(sid,oi.sid) &&
                Objects.equal(seatId,oi.seatId) && Objects.equal(order, oi.order);
    }

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("price", price)
                .add("discount",discount)
                .add("seance_id",sid)
                .add("seat_id",seatId)
                .add("order_id",order.getOid())
                .toString();
    }
}
