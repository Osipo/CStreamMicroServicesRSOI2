package ru.osipov.deploy.models;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@ToString
public class CinemaInfo {

    @Getter
    private final Long id;

    @Getter
    private final String name;

    @Getter
    private final String country;
    @Getter
    private final String city;
    @Getter
    private final String region;
    @Getter
    private final String street;

    @Getter
    private final List<RoomInfo> rooms;

    public CinemaInfo(Long id, String name, String country, String city, String region, String street, List<RoomInfo> rooms){
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.region = region;
        this.street = street;
        this.rooms = rooms;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CinemaInfo ci = (CinemaInfo) o;
        return  Objects.equal(id,ci.id) && Objects.equal(name, ci.name) &&
                Objects.equal(city,ci.city) && Objects.equal(street, ci.street) &&
                (region == null) ? ci.region == null : Objects.equal(region, ci.region) &&
                Objects.equal(country, ci.country)
                && Objects.equal(rooms,ci.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,name,city,street, region != null ? region : 0,country,rooms);
    }
}
