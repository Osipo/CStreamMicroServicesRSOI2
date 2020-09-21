package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
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

    @Transient
    private String hashId;
    
    public Order(){
        this.hashId = UUID.randomUUID().toString();
    }
    
    @Override
    public int hashCode(){
        if(oid == null || oid == 0L)
            return Objects.hashCode(hashId);
        return Objects.hashCode(oid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order oi = (Order) o;
        long id1 = oi.oid;
        long id2 = oid;
        return Objects.equal(hashId, oi.hashId);
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
