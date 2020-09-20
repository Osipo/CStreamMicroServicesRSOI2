package ru.osipov.deploy.entities;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.UUID;
import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(mappedBy = "cinema",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms;

    @Transient
    private String hashId;
   
    
    public Cinema(){
        this.hashId = UUID.randomUUID().toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cinema c = (Cinema) o;
        return Objects.equal(hashId, c.hashId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hashId);
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("cname", cname)
                .add("country", country)
                .add("city",city)
                .add("region",region)
                .add("street",street)
                .add("rooms",rooms.toString())
                .toString();
    }

}
