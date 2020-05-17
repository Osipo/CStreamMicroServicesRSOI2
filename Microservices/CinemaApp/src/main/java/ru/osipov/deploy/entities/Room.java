package ru.osipov.deploy.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import com.google.common.base.Objects;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name= "rid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "seats", nullable = false)
    private Integer seats;

    @ManyToOne
    @JoinColumn(name="cid",nullable = false)
    private Cinema cinema;

    @Override
    public int hashCode(){
        return Objects.hashCode(rid,category,seats,cinema);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room r = (Room) o;
        return Objects.equal(rid, r.rid) && Objects.equal(category,r.category) &&
                Objects.equal(seats,r.seats) && Objects.equal(cinema, r.cinema);
    }

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("rid", rid)
                .add("category", category)
                .add("seats",seats)
                .add("cinema",cinema.getCid())
                .toString();
    }
}
