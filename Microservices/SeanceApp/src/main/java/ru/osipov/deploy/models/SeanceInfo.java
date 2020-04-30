package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.osipov.deploy.entities.Seance;

import java.time.LocalDate;

@Accessors(chain = true)
@ToString
public class SeanceInfo {
    @Getter
    private final Long cid;
    @Getter
    private final Long fid;
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @Getter
    private final Long tid;

    public SeanceInfo(Long cid,Long fid,LocalDate ld, Long tid){
        this.cid = cid;
        this.fid = fid;
        this.date = ld;
        this.tid = tid;
    }

    public SeanceInfo(Long cid, Long fid,LocalDate ld){
        this(cid,fid,ld,1l);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeanceInfo si = (SeanceInfo) o;
        return  Objects.equal(cid,si.cid) && Objects.equal(fid,si.fid) && Objects.equal(tid,si.tid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cid,fid,tid);
    }
}
