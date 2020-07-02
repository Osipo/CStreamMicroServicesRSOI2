package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "rooms_cinema")
public class RoomsCinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long rid;


    @Column(name = "cid", nullable = false)
    private Long cid;

    @Override
    public int hashCode(){
        return Objects.hashCode(rid,cid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomsCinema r = (RoomsCinema) o;
        return Objects.equal(rid, r.rid) && Objects.equal(cid,r.cid);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("cid",cid)
                .add("rid",rid)
                .toString();
    }
}
