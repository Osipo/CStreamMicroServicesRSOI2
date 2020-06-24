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
public class CreateRoom {
    @NonNull
    @NotBlank
    @Size(min = 6)
    private String category;

    @NonNull
    @NotBlank
    @NotEmpty
    @Min(value = 0)
    private Integer seats;

    @Min(value = 0)
    private Integer roomNum;

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("category", category)
                .add("number",roomNum)
                .add("seats", seats).toString();
    }
}
