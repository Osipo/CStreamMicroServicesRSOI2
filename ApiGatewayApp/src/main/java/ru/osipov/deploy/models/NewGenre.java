package ru.osipov.deploy.models;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

@Data
@Accessors(chain = true)
public class NewGenre {
    @Getter
    @NonNull
    @Min(-1)
    private Long val;

    public NewGenre(){

    }
    public NewGenre(Long val){
        this.val = val;
    }
}
