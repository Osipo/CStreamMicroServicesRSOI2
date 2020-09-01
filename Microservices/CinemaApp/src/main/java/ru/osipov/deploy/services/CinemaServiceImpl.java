package ru.osipov.deploy.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.repositories.CinemaRepository;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


public class CinemaServiceImpl implements CinemaService {

    protected final CinemaRepository rep;

    private static final Logger logger = getLogger(CinemaServiceImpl.class);

    @Autowired
    public CinemaServiceImpl(CinemaRepository r){
        this.rep = r;
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getAllCinemas() {
        logger.info("Get all cinemas");
        return rep.findAll().stream().map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public CinemaInfo getCinemaById(Long id) {
        logger.info("Get cinema by id = '{}'",id);
        Optional<Cinema> o = rep.findByCid(id);
        if(o.isPresent()){
            logger.info("Film was found. Return object.");
            return ModelBuilder.buildCinemaInfo(o.get());
        }
        else{
            logger.info("Film was not found. Throw exception...");
            throw new IllegalStateException("Film was not found.");
        }
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public CinemaInfo getByName(String ciName) {
        logger.info("Get one cinema by name = '{}'",ciName);
        return rep.findByCname(ciName).map(ModelBuilder::buildCinemaInfo).orElse(new CinemaInfo(-2l,"","","","","",new ArrayList<>()));
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByCountry(String country) {
        logger.info("Get cinemas by country = '{}'",country);
        return rep.findByCountry(country).stream().map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByRegion(String region) {
        logger.info("Get cinemas by region = '{}'",region);
        return rep.findByRegion(region).stream().map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByCity(String city) {
        logger.info("Get cinemas by city = '{}'",city);
        return rep.findByCity(city).stream().map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByStreet(String street) {
        logger.info("Get cinemas by street = '{}'",street);
        return rep.findByStreet(street).stream().map(ModelBuilder::buildCinemaInfo).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CinemaInfo updateCinema(Long id, CreateCinema data) {
        logger.info("Update cinema with id = '{}'",id);
        Optional<Cinema> o = rep.findByCid(id);
        if(o.isPresent()){
            Cinema c = o.get();
            logger.info("Found successful.");
            c.setCname(data.getName());
            c.setCountry(data.getCountry());
            c.setCity(data.getCity());
            c.setRegion(data.getRegion());
            c.setStreet(data.getStreet());
            logger.info("New data was set.");
            rep.save(c);
            logger.info("Successful saved.");
            return ModelBuilder.buildCinemaInfo(c);
        }
        else{
            logger.info("Cinema with id '{}' was not found.",id);
            throw new IllegalStateException("Cinema with id"+id+" was not found.");
        }
    }

    @Override
    @Nonnull
    @Transactional
    public URI createCinema(@Nonnull CreateCinema data){
        logger.info("Creating cinema...");
        logger.info("Gathered data from form:");
        logger.info("'{}'", data.toString());
        Cinema c = new Cinema()
                    .setCname(data.getName())
                    .setCountry(data.getCountry())
                    .setCity(data.getCity())
                    .setRegion(data.getRegion())
                    .setStreet(data.getStreet());
        c = rep.save(c);
        logger.info("Successful created.");
        return URI.create("/v1/cinemas/" + c.getCid());
    }

    @Transactional
    @Override
    public CinemaInfo deleteCinema(@NonNull Long id) throws IllegalStateException{
        logger.info("Delete cinema by id =  '{}'",id);
        Optional<Cinema> o = rep.findByCid(id);
        if(o.isPresent()){
            logger.info("Cinema was found");
            CinemaInfo c = ModelBuilder.buildCinemaInfo(o.get());
            rep.delete(o.get());
            logger.info("Deleted successful!");
            return c;
        }
        else{
            logger.info("Cinema was not found.");
            throw new IllegalStateException("Cinema with id "+id+" was not found.");
        }
    }

//    @Nonnull
//    protected CinemaInfo buildModel(@Nonnull Cinema ci) {
//        logger.info("Cinema: '{}'",ci);
//        return new CinemaInfo(ci.getCid(),ci.getCname(), ci.getCountry(),ci.getCity(),ci.getRegion(),ci.getStreet());
//    }
}