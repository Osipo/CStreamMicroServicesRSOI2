package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.repositories.CinemaRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service("CinemaWithRoomsSeviceImpl")
@Component
public class CinemaWithRoomsSeviceImpl extends CinemaServiceImpl implements CinemaWithRoomsService {

    private static final Logger logger = getLogger(CinemaWithRoomsSeviceImpl.class);

    public CinemaWithRoomsSeviceImpl(CinemaRepository r) {
        super(r);
    }


    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> findByCategory(String cat) {
        logger.info("Get cinemas by room category");
        return rep.findAll().stream().filter(x -> x.getRooms().stream().filter(y -> y.getCategory().equals(cat)).count() > 0).map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> findBySeats(int seats) {
        logger.info("Get cinemas by room seats count");
        return rep.findAll().stream().filter(x -> x.getRooms().stream().filter(y -> y.getSize() >= seats).count() > 0).map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }


}
