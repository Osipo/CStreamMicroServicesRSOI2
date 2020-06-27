package ru.osipov.deploy.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate createdTime;
    
    @Column(name= "updatedAt", nullable = false)
    private LocalDate updatedAt;

    @Column(name= "ostatus", nullable = false)
    private String status;

    @Column(name= "uid", nullable = false)
    private Long uid;

}
