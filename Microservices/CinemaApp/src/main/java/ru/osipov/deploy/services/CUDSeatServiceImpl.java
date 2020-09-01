package ru.osipov.deploy.services;

import org.bouncycastle.math.raw.Mod;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.entities.Room;
import ru.osipov.deploy.entities.Seat;
import ru.osipov.deploy.models.ChangeSeatState;
import ru.osipov.deploy.models.CreateSeat;
import ru.osipov.deploy.models.SeatInfo;
import ru.osipov.deploy.repositories.RoomRepository;
import ru.osipov.deploy.repositories.SeatRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service("CUDSeatServiceImpl")
public class CUDSeatServiceImpl extends SeatServiceImpl implements CUDSeatService {

    private static final Logger logger = getLogger(CUDSeatServiceImpl.class);

    private final RoomRepository rrep;

    @Autowired
    public CUDSeatServiceImpl(SeatRepository s, RoomRepository r) {
        super(s);
        this.rrep = r;
    }

    @Override
    public URI createSeat(long rid, CreateSeat req) {
        Optional<Room> o = rrep.findById(rid);
        Room r = null;
        if(o.isPresent()){
            r = o.get();
        }
        else
            throw new IllegalArgumentException("rid is referencing on non-existing room!");
        logger.info("Creating seat in room with id '{}'",rid);
        logger.info("Gathered data from form:");
        logger.info("'{}'", req.toString());
        Seat s = new Seat()
                .setState(req.getState())
                .setNum(req.getNum())
                .setRoom(r);

        s = rep.save(s);//seat repository.

        logger.info("Successful created.");
        return URI.create("/v1/cinemas/"+r.getCinema().getCid()+"/rooms/" + r.getRid()+"/"+s.getSid());
    }

    @Override
    public SeatInfo updateSeat(long id, CreateSeat data) {
        logger.info("Update seat with id = '{}'",id);
        Optional<Seat> o = rep.findById(id);
        if(o.isPresent()){
            Seat s = o.get();
            logger.info("Found successful.");
            s.setState(data.getState());
            s.setNum(data.getNum());
            Optional<Room> or = rrep.findByRid(data.getRid());
            if(or.isPresent()) {
                Room r = or.get();
                s.setRoom(r);
                logger.info("New data was set.");
                rep.save(s);
                logger.info("Successful saved.");
                return ModelBuilder.buildSeatInfo(s);
            }
            else{
                logger.info("New room for seat with id '{}' was not found. Cannot create seat without room! ",id);
                throw new IllegalStateException("Cannot find new room. Cannot create seat without room! ");
            }
        }
        else{
            logger.info("Seat with id '{}' was not found.",id);
            throw new IllegalStateException("Seat with id"+id+" was not found.");
        }
    }


    @Override
    public List<SeatInfo> setNewState(List<ChangeSeatState> selected){
        logger.info("Set new state for selected seats.");
        List<Seat> seats = new ArrayList<>();
        List<SeatInfo> res = new ArrayList<>();
        for(ChangeSeatState s : selected){
            Optional<Seat> o = rep.findBySid(s.getSid());
            if(o.isPresent()){
                Seat si = o.get();
                si.setState(s.getNewState());
                seats.add(si);
                res.add(ModelBuilder.buildSeatInfo(si));
            }
            else{
                throw new IllegalStateException("Cannot select seats. One of them or all are booked!");
            }
        }
        logger.info("All seats are free now.");
        rep.saveAll(seats);
        logger.info("succesfull changed.");
        return res;
    }

    @Override
    public SeatInfo deleteSeat(long id) {
        logger.info("Delete seat with id '{}'",id);
        Optional<Seat> o = rep.findBySid(id);
        if(o.isPresent()){
            Seat s = o.get();
            SeatInfo res = ModelBuilder.buildSeatInfo(s);
            logger.info("Seat found successful!");
            rep.delete(s);
            logger.info("Seat deleted successful!");
            return res;
        }
        else{
            logger.info("Seat with id '{}' was not found.",id);
            throw new IllegalStateException("Seat with id"+id+"was not found.");
        }
    }
}
