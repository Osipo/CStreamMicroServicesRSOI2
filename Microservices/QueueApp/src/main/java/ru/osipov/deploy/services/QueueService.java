package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.entities.QueueItemCinema;
import ru.osipov.deploy.entities.QueueItemSeance;
import ru.osipov.deploy.models.UpdateCinema;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.repositories.QueueItemCinemaRepository;
import ru.osipov.deploy.repositories.QueueItemSeanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class QueueService implements IQueue{
    private BlockingQueue<UpdateCinema> queue;

    private final QueueItemSeanceRepository rep;
    private final QueueItemCinemaRepository rep2;

    private static final Logger logger = getLogger(QueueService.class);

    @Autowired
    public QueueService(QueueItemSeanceRepository r, QueueItemCinemaRepository r2){
        this.rep = r;
        this.rep2 = r2;
        this.queue = new LinkedBlockingQueue<UpdateCinema>();
        init();
    }


    //Load from saved db.
    private void init(){
        logger.info("Load saved data");
        List<QueueItemCinema> all = rep2.findAll();
        List<CreateSeance> seances = new ArrayList<>(10);
        for(QueueItemCinema cinema : all){
            List<QueueItemSeance> s = rep.getByQid(cinema.getQid());
            for(QueueItemSeance seance : s) {
                CreateSeance mseance = new CreateSeance(seance.getCid(), seance.getFid(), seance.getDate());
                seances.add(mseance);
            }
            UpdateCinema c = new UpdateCinema(cinema.getQid(),cinema.getCname(), cinema.getCountry(), cinema.getCity(),cinema.getRegion(),cinema.getStreet(),(CreateSeance[])seances.toArray());
            queue.add(c);
        }
    }

    private void save(UpdateCinema c){
        logger.info("Save data from request to queue.");
        QueueItemCinema cinema = new QueueItemCinema()
                .setQid(c.getCid())
                .setCname(c.getName())
                .setCountry(c.getCountry())
                .setCity(c.getCity())
                .setRegion(c.getRegion())
                .setStreet(c.getStreet());

        CreateSeance[] seances = c.getSeances();
        rep2.save(cinema);
        logger.info("cinema saved with Id: ",cinema.getQid());
        for(CreateSeance seance: seances){
            QueueItemSeance s = new QueueItemSeance(seance.getCid(),seance.getFid(),seance.getDate(),cinema);
            rep.save(s);
        }
        logger.info("Data are saved in db.");
    }

    public void push(UpdateCinema c){
        save(c);
        queue.add(c);
    }

    public UpdateCinema get(){
        return queue.poll();
    }

    public Integer size(){
        return queue.size();
    }

    public Queue<UpdateCinema> getQueue(){
        return this.queue;
    }
}
