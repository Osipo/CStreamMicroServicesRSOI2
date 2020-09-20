package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeat {

    @NonNull
    @NotBlank
    @Size(min = 2)
    private String state;

    @NonNull
    @NotBlank
    @Min(value = 1)
    private Long num;

    @NonNull
    @NotBlank
    private Long rid;

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("state", state)
                .add("number", num)
                .add("rid",rid).toString();
    }
}
