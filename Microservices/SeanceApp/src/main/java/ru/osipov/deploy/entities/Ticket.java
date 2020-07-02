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
@IdClass(TicketPK.class)
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {


    @Id
    @Column(name = "seat_id",nullable = false)
    private Long seatId;

    @Column(name = "price",nullable = false)
    private Double price;

    @Column(name = "payment_type",nullable = false)
    private String ptype;

    @Id
    @ManyToOne
    @JoinColumns ({
            @JoinColumn(name="sid", referencedColumnName = "sid"),
            @JoinColumn(name="rid", referencedColumnName = "room_id")
    })
    private Seance sid;


    public Seance getSeance(){
        return sid;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket t = (Ticket) o;
        return  Objects.equal(seatId, t.seatId) && Objects.equal(sid,t.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(seatId,sid);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("seatId",seatId)
                .add("price",price)
                .add("payment",ptype)
                .add("seance",sid)
                .toString();
    }
}
