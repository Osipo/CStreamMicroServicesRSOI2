package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.osipov.deploy.entities.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOid(Long oid);
    List<Order> findByStatus(String status);
    List<Order> findByStatusAndUid(String status, Long uid);
    List<Order> findByUid(Long uid);
    List<Order> findBySum(Double sum);
    List<Order> findByCreatedTime(LocalTime time);
    List<Order> findByCreatedAt(LocalDate created);
    List<Order> findByUpdatedAt(LocalDate updated);

    @Query("Select o from OrderItem it join it.order o where it.discount = ?1")
    List<Order> findByDiscount(Double discount);
}
