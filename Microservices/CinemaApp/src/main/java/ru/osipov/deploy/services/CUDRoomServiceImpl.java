package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.entities.Room;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateRoom;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.repositories.CinemaRepository;
import ru.osipov.deploy.repositories.RoomRepository;

import java.net.URI;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CUDRoomServiceImpl extends RoomServiceImpl implements CUDRoomService {

    private static final Logger logger = getLogger(CUDRoomServiceImpl.class);

    private final CinemaRepository crep;

    @Autowired
    public CUDRoomServiceImpl(RoomRepository r,CinemaRepository r2) {
        super(r);
        this.crep = r2;
    }

    @Override
    public URI createRoom(long cid, CreateRoom req) {
        Optional<Cinema> o = crep.findByCid(cid);
        Cinema c = null;
        if(o.isPresent()){
            c = o.get();
        }
        else
            throw new IllegalArgumentException("cid is referencing on non-existing cinema!");
        logger.info("Creating room in cinema with cid '{}'",cid);
        logger.info("Gathered data from form:");
        logger.info("'{}'", req.toString());
        Room r = new Room()
                .setCategory(req.getCategory())
                .setSize(req.getSeats())
                .setCinema(c);
        r = rep.save(r);
        logger.info("Successful created.");
        return URI.create("/v1/cinemas/"+cid+"/rooms/" + r.getRid());
    }

    @Override
    public RoomInfo updateRoom(long id, CreateRoom data) {
        logger.info("Update room with id = '{}'",id);
        Optional<Room> o = rep.findById(id);
        if(o.isPresent()){
            Room r = o.get();
            logger.info("Found successful.");
            r.setCategory(data.getCategory());
            r.setSize(data.getSeats());
            logger.info("New data was set.");
            rep.save(r);
            logger.info("Successful saved.");
            return ModelBuilder.buildRoomInfo(r);
        }
        else{
            logger.info("Room with id '{}' was not found.",id);
            throw new IllegalStateException("Room with id"+id+" was not found.");
        }
    }

    @Override
    public RoomInfo deleteRoom(long id) {
        logger.info("Delete room by id =  '{}'",id);
        Optional<Room> o = rep.findById(id);
        if(o.isPresent()){
            logger.info("Room was found");
            RoomInfo r = ModelBuilder.buildRoomInfo(o.get());
            rep.delete(o.get());
            logger.info("Deleted successful!");
            return r;
        }
        else{
            logger.info("Room was not found.");
            throw new IllegalStateException("Room with id "+id+" was not found.");
        }
    }
}
