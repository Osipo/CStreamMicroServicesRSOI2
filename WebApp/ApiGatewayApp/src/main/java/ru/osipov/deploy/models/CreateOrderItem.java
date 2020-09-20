package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItem {

    @NonNull
    @NotBlank
    @Min(value = 0)
    private Double price;

    @NonNull
    @NotBlank
    @Min(value = 0)
    private Double discount;

    @NonNull
    @NotBlank
    @Min(value = 1)
    private Long seatId;

    @NonNull
    @NotBlank
    @Min(value = 1)
    private Long seanceId;

    @Override
    public String toString(){
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("price", price)
                .add("discount", discount)
                .add("seanceId",seanceId)
                .add("seatId",seatId).toString();
    }
}
