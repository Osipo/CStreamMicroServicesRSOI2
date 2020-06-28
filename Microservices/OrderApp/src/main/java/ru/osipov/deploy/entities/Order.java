package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "customer_orders")
public class Order {
    @Id
    @Column(name= "oid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @Column(name= "totalSum", nullable = false)
    private Double sum;

    @Column(name= "createdAt", nullable = false)
    private LocalDate createdAt;
    
    @Column(name= "creationTime", nullable = false)
    private LocalTime createdTime;
    
    @Column(name= "updatedAt", nullable = false)
    private LocalDate updatedAt;

    @Column(name= "ostatus", nullable = false)
    private String status;

    @Column(name= "uid", nullable = false)
    private Long uid;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> items;

    @Override
    public int hashCode(){
        return Objects.hashCode(oid,sum,createdAt,createdTime,updatedAt,status,uid,items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order oi = (Order) o;
        return Objects.equal(oid, oi.oid) && Objects.equal(sum,oi.sum) &&
                Objects.equal(uid,oi.uid) && Objects.equal(status, oi.status) &&
                Objects.equal(createdAt,oi.createdAt) &&
                Objects.equal(updatedAt,oi.updatedAt) &&
                Objects.equal(createdTime,oi.createdTime) && Objects.equal(items, oi.items);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("oid", oid)
                .add("sum", sum)
                .add("createdAt", createdAt)
                .add("timeCreation", createdTime)
                .add("updatedAt", updatedAt)
                .add("status", status)
                .add("user_id", uid)
                .add("orderItems", items)
                .toString();
    }

}
