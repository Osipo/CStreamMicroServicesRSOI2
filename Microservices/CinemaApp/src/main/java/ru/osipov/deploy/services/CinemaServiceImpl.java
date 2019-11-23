package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.repositories.CinemaRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository rep;

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
        return rep.findAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public CinemaInfo getCinemaById(Long id) {
        logger.info("Get cinema by id = '{}'",id);
        Optional<Cinema> o = rep.findByCid(id);
        if(o.isPresent()){
            logger.info("Film was found. Return object.");
            return buildModel(o.get());
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
        return rep.findByCname(ciName).map(this::buildModel).orElse(new CinemaInfo(-1l,"","","","",""));
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByCountry(String country) {
        logger.info("Get cinemas by country = '{}'",country);
        return rep.findByCountry(country).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByRegion(String region) {
        logger.info("Get cinemas by region = '{}'",region);
        return rep.findByRegion(region).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByCity(String city) {
        logger.info("Get cinemas by city = '{}'",city);
        return rep.findByCity(city).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<CinemaInfo> getByStreet(String street) {
        logger.info("Get cinemas by street = '{}'",street);
        return rep.findByStreet(street).stream().map(this::buildModel).collect(Collectors.toList());
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
            return buildModel(c);
        }
        else{
            logger.info("Cinema with id '{}' was not found.",id);
            throw new IllegalStateException("Cinema with id"+id+" was not found.");
        }
    }

    @Nonnull
    private CinemaInfo buildModel(@Nonnull Cinema ci) {
        logger.info("Cinema: '{}'",ci);
        return new CinemaInfo(ci.getCid(),ci.getCname(), ci.getCountry(),ci.getCity(),ci.getRegion(),ci.getStreet());
    }
}