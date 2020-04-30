package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "seance")
@IdClass(SeancePK.class)
@AllArgsConstructor
@NoArgsConstructor
public class Seance {

    @Id
    @Column(name = "cid", nullable = false)
    private Long cid;

    @Id
    @Column(name = "fid", nullable = false)
    private Long fid;

    @Column(name = "begining", nullable = false)
    private LocalDate date;

    @Column(name = "tid",nullable = false)
    private Long tid;

    @OneToOne
    @JoinColumn(name = "tid")
    private Ticket ticket;

    public Seance(Long cid, Long fid){
        this.cid = cid;
        this.fid = fid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seance s = (Seance) o;
        return  Objects.equal(cid, s.cid) &&
                Objects.equal(fid,s.fid) &&
                Objects.equal(date,s.date) &&
                Objects.equal(tid,s.tid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cid,fid,date,tid);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("cid",cid)
                .add("fid",fid)
                .add("date",date)
                .add("tid",tid)
                .toString();
    }
}
