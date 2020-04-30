package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osipov.deploy.entities.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByPrice(Double price);
}
