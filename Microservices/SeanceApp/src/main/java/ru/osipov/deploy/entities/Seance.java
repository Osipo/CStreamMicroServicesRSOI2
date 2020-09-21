package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "seance")
@IdClass(SeancePK.class)
@AllArgsConstructor
@NoArgsConstructor
public class Seance {

    @Id
    @Column(name = "sid", nullable = false)
    private Long sid;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomsCinema rid;


    @Column(name = "fid", nullable = false)
    private Long fid;

    @Column(name = "begining_date", nullable = false)
    private LocalDate date;

    @Column(name = "begining_time", nullable = false)
    private LocalTime time;

    @OneToMany(mappedBy = "sid")
    private Set<Ticket> tickets;

    @Transient
    private String hashId;
    
    public Seance(){
        this.hashId = UUID.randomUUID().toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seance s = (Seance) o;
        if((sid == null || sid == 0L) && (rid == null || rid == 0L))
            return Objects.equal(hashId, s.hashId);
        return  Objects.equal(sid, s.sid) &&
                Objects.equal(fid,s.fid) &&
                Objects.equal(rid, s.rid) &&
                Objects.equal(date,s.date) &&
                Objects.equal(time,s.time);
    }

    @Override
    public int hashCode() {
        if(sid == null || sid == 0L)
            return Objects.hashCode(hashId);
        return Objects.hashCode(sid,rid);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("seanceId",sid)
                .add("filmId",fid)
                .add("date",date)
                .add("time",time)
                .add("cinemaId",rid.getCid())
                .toString();
    }
}
