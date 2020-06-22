package ru.osipov.deploy.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Seat;
import ru.osipov.deploy.models.SeatInfo;
import ru.osipov.deploy.repositories.SeatRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SeatServiceImpl implements SeatService{

    protected final SeatRepository rep;

    private static final Logger logger = getLogger(SeatServiceImpl.class);

    @Autowired
    public SeatServiceImpl(SeatRepository r){
        this.rep = r;
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public SeatInfo getSeatById(long sid){
        logger.info("Get seat by id = '{}'",sid);
        Optional<Seat> o = rep.findById(sid);
        if(o.isPresent()){
            logger.info("Seat was found. Return object.");
            return ModelBuilder.buildSeatInfo(o.get());
        }
        else{
            logger.error("Seat was not found. Throw exception...");
            throw new IllegalStateException("Seat was not found.");
        }
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<SeatInfo> getAllSeats(){
        logger.info("Get ALL seats");
        return rep.findAll().stream().map(ModelBuilder::buildSeatInfo).collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatByState(String state){
        logger.info("Get seats by their state = '{}'",state);
        return rep.findByState(state).stream().map(ModelBuilder::buildSeatInfo).collect(Collectors.toList());
    }


    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatByNum(long num){
        logger.info("Get seats by their number = '{}'",num);
        return rep.findByNum(num).stream().map(ModelBuilder::buildSeatInfo).collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatsByRoom(long rid) {
        logger.info("Get seats by their room = '{}'",rid);
        return rep.findByRoomId(rid).stream().map(ModelBuilder::buildSeatInfo).collect(Collectors.toList());
    }



    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatsByRoomAndState(long rid,String state){
        logger.info("Get available seat (with status '{}') in room '{}'",state,rid);
        return rep.findByRoomId(rid).stream().filter(x -> x.getState().equals(state)).map(ModelBuilder::buildSeatInfo).collect(Collectors.toList());
    }
}
