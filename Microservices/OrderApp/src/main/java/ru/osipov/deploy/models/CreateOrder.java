package ru.osipov.deploy.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate created;
    @JsonFormat(pattern = "HH:mm:ss.SS")
    private LocalTime ctime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updated;

    private List<CreateOrderItem> items;

}
