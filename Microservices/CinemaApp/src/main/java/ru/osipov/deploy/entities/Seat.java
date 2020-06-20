package ru.osipov.deploy.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import com.google.common.base.Objects;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @Column(name= "seat_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    @Column(name = "num",nullable = false)
    private Long num;

    @Column(name = "state",nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(name="room_id",nullable = false)
    private Room room;

    @Override
    public int hashCode(){
        return Objects.hashCode(sid,num,state,room);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat s = (Seat) o;
        return Objects.equal(sid, s.sid) && Objects.equal(num,s.num) &&
                Objects.equal(state,s.state) && Objects.equal(room, s.room);
    }

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("rid", room.getRid())
                .add("number", num)
                .add("state",state)
                .add("seat_id",sid)
                .toString();
    }
}
