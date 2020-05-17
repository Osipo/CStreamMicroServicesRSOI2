package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.Ticket;

import java.util.List;

@Repository("TicketRepository")
public interface TicketRepository extends  JpaRepository<Ticket,Long> {
    List<Ticket> findByPrice(Double price);

}