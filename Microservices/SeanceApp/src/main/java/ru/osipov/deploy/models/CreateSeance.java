package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class CreateSeance {

    @Nonnull
    @Min(-1)
    private  Long cid;

    @Nonnull
    @Min(-1)
    private  Long fid;

    @Nonnull
    @Min(-1)
    private Long rid;

    @Nonnull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Nonnull
    @JsonFormat(pattern = "HH:mm:ss.SS")
    private LocalTime time;


    @ConstructorProperties({"cid","fid","rid","ld","lt"})
    public CreateSeance(Long cid,Long fid,Long rid,LocalDate ld,LocalTime lt){
        this.cid = cid;
        this.fid = fid;
        this.rid = rid;
        this.date = ld;
        this.time = lt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateSeance si = (CreateSeance) o;
        return  Objects.equal(cid,si.cid) && Objects.equal(fid,si.fid) && Objects.equal(rid,si.rid)
                && Objects.equal(date,si.date) && Objects.equal(time,si.time);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cid,rid,fid,date,time);
    }
}
