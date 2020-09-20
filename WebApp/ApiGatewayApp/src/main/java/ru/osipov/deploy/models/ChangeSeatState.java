package ru.osipov.deploy.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSeatState {

    @NonNull
    @NotBlank
    private Long sid;

    @NonNull
    @NotBlank
    @Size(min = 2)
    private String oldstate;

    @NonNull
    @NotBlank
    @Size(min = 2)
    private String newState;


    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("oldState", oldstate)
                .add("newState",newState)
                .add("sid", sid).toString();
    }
}
