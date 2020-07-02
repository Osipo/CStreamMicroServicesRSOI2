package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.configuration.ApplicationContextProvider;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.Ticket;
import ru.osipov.deploy.models.TicketInfo;
import ru.osipov.deploy.repositories.SeanceRepository;
import ru.osipov.deploy.repositories.TicketRepository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service("TicketProcedures")
public class TicketServiceProcImpl extends TicketServiceImpl implements TicketService {


    private static final Logger logger = getLogger(TicketServiceProcImpl.class);

    @Autowired
    public TicketServiceProcImpl(@Qualifier("TicketRepositoryProc") TicketRepository rep, SeanceRepository srep){
        super(rep,srep);
    }

    @Override
    public List<TicketInfo> getAllTicketsByPrice(double price) {
        logger.info("Get all tickets by price proc call:: "+price);
        List<Ticket> tickets = trep.findByPrice(price);
        List<TicketInfo> ticketsinfo = new ArrayList<>();
        for(Ticket t : tickets){
            ticketsinfo.add(
                    new TicketInfo(t.getSeance().getSid(),t.getSeance().getFid(),
                            t.getSeance().getRid().getCid(),t.getSeance().getRid().getRid(),
                            t.getSeatId(),t.getPrice(),t.getPtype()));
        }
        return ticketsinfo;
    }
}
