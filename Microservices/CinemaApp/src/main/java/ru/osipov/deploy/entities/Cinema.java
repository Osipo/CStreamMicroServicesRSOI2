package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "cinema")
public class Cinema {
    @Id
    @Column(name= "cid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @Column(name = "cname", nullable = false)
    private String cname;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "region", nullable = true)
    private String region;

    @Column(name = "country", nullable = false)
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cinema c = (Cinema) o;
        return Objects.equal(cid, c.cid) && Objects.equal(cname, c.cname) &&
                Objects.equal(city,c.city) && Objects.equal(street, c.street) &&
                (region == null) ? c.region == null : Objects.equal(region, c.region) &&
                Objects.equal(country, c.country);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cid, cname,city,street, region != null ? region : 0,country);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("cname", cname)
                .add("country", country)
                .add("city",city)
                .add("region",region)
                .add("street",street)
                .toString();
    }

}
