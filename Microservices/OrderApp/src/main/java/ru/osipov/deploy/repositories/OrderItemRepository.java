package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.osipov.deploy.entities.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByDiscount(Double discount);
    List<OrderItem> findByPrice(Double price);
    List<OrderItem> findByDiscountAndPrice(Double discount, Double price);
    List<OrderItem> findBySid(Long sid);
    List<OrderItem> findBySeatId(Long seatId);
    Optional<OrderItem> findBySeatIdAndSid(Long seatId, Long sid);

    @Query("select it from Order o join o.items it where o.sum = ?1")
    List<OrderItem> findByTotalSum(Double sum);
}
