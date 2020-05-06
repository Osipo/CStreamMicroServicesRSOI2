package ru.osipov.deploy.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.Ticket;

import java.util.List;

@Repository("TicketRepositoryProc")
public interface TicketRepositoryProc extends TicketRepository {

    @Override
    @Query(name = "Ticket.GetTicketsByPrice", value="CALL GetTicketsByPrice(:price)",nativeQuery = true)
    List<Ticket> findByPrice(@Param("price") Double price);
}
