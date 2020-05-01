package ru.osipov.deploy.services;

import lombok.NonNull;
import ru.osipov.deploy.entities.Ticket;
import ru.osipov.deploy.models.TicketInfo;

import java.util.List;

public interface TicketService {
    @NonNull
    public List<TicketInfo> getAllTickets();

    @NonNull
    public List<TicketInfo> getAllTicketsByPrice(double price);
}
