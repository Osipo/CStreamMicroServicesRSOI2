package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@NamedStoredProcedureQuery(
        name="Ticket.GetTicketsByPrice",
        procedureName="GetTicketsByPrice",
        resultClasses = { Ticket.class },
        parameters={
                @StoredProcedureParameter(name="pr", type=Double.class, mode=ParameterMode.IN)
        }
)
@Table(name = "ticket")
@NoArgsConstructor
public class Ticket {

    @Id
    @Column(name = "tid", nullable = false)
    private Long tid;

    @Column(name = "price",nullable = false)
    private Double price;

    public Ticket(Long tid, Double price){
        this.tid = tid;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket t = (Ticket) o;
        return  Objects.equal(tid, t.tid) &&
                Objects.equal(price,t.price);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tid,price);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("tid",tid)
                .add("price",price)
                .toString();
    }
}
