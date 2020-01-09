package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "queue_item_seances")
@Data
@Accessors(chain = true)
@IdClass(SeancePK.class)
@NoArgsConstructor
public class QueueItemSeance {


    @Id
    @Column(name = "cid", nullable = false)
    private Long cid;

    @Id
    @Column(name = "fid", nullable = false)
    private Long fid;

    @Column(name = "begining", nullable = false)
    private LocalDate date;

    @Column(name = "qid", nullable = false, insertable = false, updatable = false)
    private Long qid;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = false, name="qid", referencedColumnName = "qid")
    private QueueItemCinema cinema;



//
//    @OneToOne
//    @JoinColumn(name = "qid", referencedColumnName = "qid")
//    public QueueItemCinema getCinema(){
//        return this.cinema;
//    }
//
//    public void setCinema(QueueItemCinema cinema){
//        this.cinema = cinema;
//    }

    public QueueItemSeance(Long cid, Long fid){
        this.cid = cid;
        this.fid = fid;
    }

    public QueueItemSeance(Long cid, Long fid, LocalDate date, QueueItemCinema cinema){
        this.cid = cid;
        this.fid = fid;
        this.date = date;
        this.cinema = cinema;
        this.cinema.setSeance(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueItemSeance s = (QueueItemSeance) o;
        return  Objects.equal(cid, s.cid) &&
                Objects.equal(fid,s.fid) &&
                Objects.equal(date,s.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cid,fid,date);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("cid",cid)
                .add("fid",fid)
                .add("date",date)
                .toString();
    }
}
