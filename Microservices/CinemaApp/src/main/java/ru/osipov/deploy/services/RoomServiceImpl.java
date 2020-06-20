package ru.osipov.deploy.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.entities.Room;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.repositories.CinemaRepository;
import ru.osipov.deploy.repositories.RoomRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RoomServiceImpl implements RoomService {

    protected final RoomRepository rep;

    private static final Logger logger = getLogger(RoomServiceImpl.class);

    @Autowired
    public RoomServiceImpl(RoomRepository r){
        this.rep = r;
    }


    @Override
    @NonNull
    @Transactional(readOnly = true)
    public RoomInfo getRoomById(long id) {
        logger.info("Get room by its id");
        Optional<Room> o = rep.findById(id);
        if(o.isPresent()){
            logger.info("Room was found. Return object.");
            return ModelBuilder.buildRoomInfo(o.get());
        }
        else{
            logger.info("Room was not found. Throw exception...");
            throw new IllegalStateException("Room was not found.");
        }
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getAllRooms() {
        logger.info("Get all rooms");
        return rep.findAll().stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getByCategory(String cat) {
        logger.info("Get rooms by category = '{}'",cat);
        return rep.findByCategory(cat).stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getBySeats(int seats) {
        logger.info("Get rooms by available seats = '{}'",seats);
        return rep.findBySeats(seats).stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getByCid(long cid){
        logger.info("Get rooms by cinema id = '{}'",cid);
        return rep.findByCid(cid).stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getByCinemaName(String name) {
        logger.info("Get rooms by cinema name = '{}'",name);
        return rep.findByCinemaName(name).stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList());
    }

    @Override
    @Nonnull
    @Transactional(readOnly = true)
    public List<RoomInfo> getBySeatNum(long num) {
        logger.info("Get rooms by seat number = '{}'",num);
        return rep.findBySeatNumber(num).stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList());
    }
}
