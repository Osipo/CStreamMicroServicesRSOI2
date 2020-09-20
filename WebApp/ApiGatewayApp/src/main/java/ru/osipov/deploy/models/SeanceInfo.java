package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Accessors(chain = true)
@ToString
public class SeanceInfo {
    @Getter
    private final Long sid;
    @Getter
    private final Long cid;
    @Getter
    private final Long rid;
    @Getter
    private final Long fid;
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @Getter
    @JsonFormat(pattern = "HH:mm:ss.SS")
    private final LocalTime time;


    public SeanceInfo(Long sid,Long cid,Long rid, Long fid,LocalDate ld,LocalTime lt){
        this.sid = sid;
        this.cid = cid;
        this.rid = rid;
        this.fid = fid;
        this.date = ld;
        this.time = lt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeanceInfo si = (SeanceInfo) o;
        return  Objects.equal(sid,si.sid) && Objects.equal(cid,si.cid) && Objects.equal(rid,si.rid) && Objects.equal(fid,si.fid) && Objects.equal(date,si.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sid,cid,rid,fid,date);
    }
}