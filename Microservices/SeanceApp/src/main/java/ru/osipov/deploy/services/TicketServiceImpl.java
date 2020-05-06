package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.osipov.deploy.configuration.SeancesInstance;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.Ticket;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.models.TicketInfo;
import ru.osipov.deploy.repositories.SeanceRepository;
import ru.osipov.deploy.repositories.TicketRepository;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class TicketServiceImpl implements TicketService {

    protected final TicketRepository trep;
    protected final SeanceRepository srep;

    private static final Logger logger = getLogger(TicketServiceImpl.class);

    @Autowired
    public TicketServiceImpl(TicketRepository rep, SeanceRepository srep){
        this.trep = rep;
        this.srep = srep;
    }

    @Override
    public List<TicketInfo> getAllTickets() {
        logger.info("Get all tickets");
        List<Seance> seances = srep.findAll();
        List<TicketInfo> tickets = new ArrayList<>();
        for(Seance s : seances){
            Ticket t = trep.getOne(s.getTid());
            tickets.add(buildModel(t,s.getFid(),s.getCid()));
        }
        return tickets;
    }

    @Override
    public List<TicketInfo> getAllTicketsByPrice(double price) {
        logger.info("Get all tickets by price:: "+price);
        List<Seance> seances = srep.findAll();
        List<TicketInfo> tickets = new ArrayList<>();
        for(Seance s : seances){
            Ticket t = trep.getOne(s.getTid());
            if(t.getPrice().equals(price))
                tickets.add(buildModel(t,s.getFid(),s.getCid()));
        }
        return tickets;
    }

    private TicketInfo buildModel(Ticket t,Long fid, Long cid){
        return new TicketInfo(t.getTid(),fid,cid,t.getPrice());
    }
}
