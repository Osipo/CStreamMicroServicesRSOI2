package ru.osipov.deploy.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.RoomsCinema;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.SeancePK;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.repositories.RoomsCinemaRepository;
import ru.osipov.deploy.repositories.SeanceRepository;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;
import java.time.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SeanceServiceImpl implements SeanceService{

    private final SeanceRepository rep;

    private final RoomsCinemaRepository r2;

    private static final Logger logger = getLogger(SeanceServiceImpl.class);

    @Autowired
    public SeanceServiceImpl(SeanceRepository r, RoomsCinemaRepository r2){
        this.rep = r; this.r2 = r2;
    }

    @Override
    @NonNull
    public List<SeanceInfo> getAllSeances() {
        logger.info("Get all...");
        return rep.findAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<SeanceInfo> getSeancesInCinema(@Nonnull Long cid) {
        logger.info("Get seance in cinema id = '{}'",cid);
        return rep.findByCid(cid).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<SeanceInfo> getSeancesByFilm(Long fid) {
        logger.info("Get seance of film with id  = '{}'",fid);
        return rep.findByFid(fid).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public SeanceInfo getSeanceByFilmAndCinema(Long fid, Long cid) {
        logger.info("Get seance of film = '{}' from cinema = '{}'",fid,cid);
        return rep.findByFidAndCid(fid,cid).map(this::buildModel).orElse(new SeanceInfo(-1l,-1l,-1l,-1l, LocalDate.now(),LocalTime.now()));
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public List<SeanceInfo> getSeancesByDate(String dateStr){
        logger.info("Date string = '{}'",dateStr);
        LocalDate date = LocalDate.parse(dateStr);
        return rep.findAllByDate(date).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceInfo> getSeancesByDateBetween(String dateStr, String dateEnd) {
        logger.info("Get all seances including from '{}' to '{}'",dateStr,dateEnd);
        LocalDate date1 = LocalDate.parse(dateStr);
        LocalDate date2 = LocalDate.parse(dateEnd);
        return rep.findAllByDateBetween(date1,date2).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceInfo> getSeancesByDateBefore(String dateStr) {
        logger.info("Get all seances til = '{}'",dateStr);
        LocalDate date = LocalDate.parse(dateStr);
        return rep.findAllByDateBefore(date).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Override
    public void deleteSeancesWithFilm(Long fid) {
        List<Seance> l = rep.findByFid(fid);
        if(l.size() == 0){
            logger.info("not found.");
            throw new IllegalStateException("Films are not found.");
        }
        else{
            logger.info("Found films");
            logger.info("Count: "+l.size());
            rep.deleteAll(l);
            logger.info("Successful deleted.");
        }
    }

    @Override
    @Transactional
    public SeanceInfo updateSeance(Long cid, Long fid, CreateSeance data) {
        Optional<Seance> o = rep.findByFidAndCid(fid,cid);
        if(o.isPresent()){
            logger.info("Seance was found.");
            Seance s = o.get();
            s.getRid().setCid(data.getCid());
            s.setFid(data.getFid());
            s.setDate(data.getDate());
            logger.info("New values are set.");
            rep.save(s);
            logger.info("Successful saved.");
            return buildModel(s);
        }
        else{
            logger.info("Seance with cinema_id = '{}' and film_id = '{}' was NOT FOUND.",cid,fid);
            throw new IllegalStateException("Seance with cinema_id = '{}' and film_id = '{]' was NOT FOUND.");
        }
    }

    @Override
    @Transactional
    public URI createSeance(CreateSeance request) {
        logger.info("Creating seance...");
        logger.info("Vals:\n\t cid = '{}'\n\t fid = '{}' \n\tdate = '{}'",request.getCid(),
                request.getFid(),request.getDate().toString());

        logger.info("Try to get Room with Cinema by their ids: '{}', '{}'",request.getCid(),request.getRid());
        Optional<RoomsCinema> o = r2.findByRid(request.getRid());
        if(o.isPresent()){
            RoomsCinema rc = o.get();
            if(rc.getCid() == request.getCid()){
                //sid, rid, fid, date, time, tickets
                Seance c = new Seance()
                        .setRid(rc)
                        .setFid(request.getFid())
                        .setDate(request.getDate())
                        .setTime(request.getTime());

                c = rep.save(c);
                logger.info("Successful created.");
                return URI.create("/v1/seances/"+c.getRid().getCid()+"/"+c.getFid());
            }
            logger.info("Cannot create seance. The cinema with id '{}' does not exist!",request.getCid());
        }
        else{
            logger.info("Cannot create seance. The room with id '{}' does not exist!",request.getRid());
        }
        return null;
    }

    private SeanceInfo buildModel(Seance s){
        return new SeanceInfo(s.getSid(),s.getRid().getCid(),s.getRid().getRid(),s.getFid(),s.getDate(),s.getTime());
    }
}
