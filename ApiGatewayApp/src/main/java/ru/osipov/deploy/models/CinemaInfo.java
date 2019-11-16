package ru.osipov.deploy.models;

import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString
public class CinemaInfo {

    @Getter
    @Expose
    private final Long id;

    @Getter
    @Expose
    private final String name;

    @Getter
    @Expose
    private final String country;
    @Getter
    @Expose
    private final String city;
    @Getter
    @Expose
    private final String region;
    @Getter
    @Expose
    private final String street;

    public CinemaInfo(){
        this(-1L,"Error","","","","");
    }
    public CinemaInfo(Long id, String name, String country, String city, String region, String street){
        this.id = id;
        this.name = name;
        this.country = country;
        this.city = city;
        this.region = region;
        this.street = street;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CinemaInfo ci = (CinemaInfo) o;
        return  Objects.equal(id,ci.id) && Objects.equal(name, ci.name) &&
                Objects.equal(city,ci.city) && Objects.equal(street, ci.street) &&
                (region == null) ? ci.region == null : Objects.equal(region, ci.region) &&
                Objects.equal(country, ci.country);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,name,city,street, region != null ? region : 0,country);
    }
}
