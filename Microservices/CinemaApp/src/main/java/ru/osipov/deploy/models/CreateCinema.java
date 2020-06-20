package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateCinema {

    @NonNull
    @NotBlank
    @Size(min = 3)
    private String name;

    @NonNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    private String country;

    @NonNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    private String city;


    private String region;

    @NonNull
    @NotBlank
    @NotEmpty
    @Size(min = 3)
    private String street;

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("country", country)
                .add("city",city)
                .add("region",region)
                .add("street",street).toString();
    }
}
