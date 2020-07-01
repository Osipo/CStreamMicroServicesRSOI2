package ru.osipov.deploy.services;

import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.OrderItem;
import ru.osipov.deploy.models.CreateOrder;
import ru.osipov.deploy.models.OrderInfo;
import ru.osipov.deploy.models.UpdateOrder;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface OrderService {

    @Nonnull
    OrderInfo getById(long oid);

    @Nonnull
    List<OrderInfo> getByCreationDate(LocalDate date);
    @Nonnull
    List<OrderInfo> getByCreationTime(LocalTime time);
    @Nonnull
    List<OrderInfo> getByUpdatedDate(LocalDate date);
    @Nonnull
    List<OrderInfo> getByStatus(String status);
    @Nonnull
    List<OrderInfo> getByUid(long uid);
    @Nonnull
    List<OrderInfo> getBySum(double sum);
    @Nonnull
    URI createOrder(CreateOrder o);
    @Nonnull
    OrderInfo updateOrder(UpdateOrder o);
    @Nonnull
    OrderInfo deleteOrder(long id);
}
