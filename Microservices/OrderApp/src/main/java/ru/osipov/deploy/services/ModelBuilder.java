package ru.osipov.deploy.services;

import ru.osipov.deploy.entities.Order;
import ru.osipov.deploy.entities.OrderItem;
import ru.osipov.deploy.models.OrderInfo;
import ru.osipov.deploy.models.OrderItemInfo;

import java.util.stream.Collectors;

public class ModelBuilder {
    public static OrderInfo createOrderInfo(Order o){
        return new OrderInfo(o.getOid(),o.getUid(),o.getSum(),o.getStatus(),o.getCreatedAt(),o.getCreatedTime(),o.getUpdatedAt(),o.getItems().stream().map(ModelBuilder::createOrderItemInfo).collect(Collectors.toList()));
    }

    public static OrderItemInfo createOrderItemInfo(OrderItem it){
        return new OrderItemInfo(it.getOrder().getOid(),it.getPrice(),it.getDiscount(),it.getSid(),it.getSeatId());
    }
}
