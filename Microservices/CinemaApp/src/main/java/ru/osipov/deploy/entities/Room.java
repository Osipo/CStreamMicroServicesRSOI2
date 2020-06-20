package ru.osipov.deploy.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import com.google.common.base.Objects;

import java.util.Set;

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
    private Integer size;

    @ManyToOne
    @JoinColumn(name="cid",nullable = false)
    private Cinema cinema;

    @OneToMany(mappedBy = "room")
    private Set<Seat> seats;

    @Override
    public int hashCode(){
        return Objects.hashCode(rid,category,size,cinema,seats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room r = (Room) o;
        return Objects.equal(rid, r.rid) && Objects.equal(category,r.category) &&
                Objects.equal(size,r.size) &&
                Objects.equal(seats,r.seats) && Objects.equal(cinema, r.cinema);
    }

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("rid", rid)
                .add("category", category)
                .add("size",size)
                .add("cinema",cinema.getCid())
                .add("seats",seats.toString())
                .toString();
    }
}
