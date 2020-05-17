package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.entities.Room;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.repositories.CinemaRepository;
import ru.osipov.deploy.repositories.RoomRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class RoomServiceImpl implements RoomService {

    private final RoomRepository rep;

    private static final Logger logger = getLogger(RoomServiceImpl.class);

    @Autowired
    public RoomServiceImpl(RoomRepository r){
        this.rep = r;
    }


    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getAllRooms() {
        logger.info("Get all rooms");
        return rep.findAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getByCategory(String cat) {
        logger.info("Get rooms by category = '{}'",cat);
        return rep.findByCategory(cat).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<RoomInfo> getBySeats(int seats) {
        logger.info("Get rooms by available seats = '{}'",seats);
        return rep.findBySeats(seats).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    private RoomInfo buildModel(@Nonnull Room ri) {
        logger.info("Room: '{}'",ri);
        return new RoomInfo(ri.getRid(),ri.getCinema().getCid(), ri.getCategory(),ri.getSeats());
    }
}
