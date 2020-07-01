package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrder {

    private Long uid;

    private Double sum;

    private String status;

    private LocalDate created;

    private LocalTime ctime;

    private LocalDate updated;

    private List<CreateOrderItem> items;

}
