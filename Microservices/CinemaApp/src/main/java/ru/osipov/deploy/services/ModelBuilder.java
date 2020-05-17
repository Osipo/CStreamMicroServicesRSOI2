package ru.osipov.deploy.services;

import org.slf4j.Logger;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.entities.Room;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.RoomInfo;

import javax.annotation.Nonnull;

import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class ModelBuilder {
    private static final Logger logger = getLogger(ModelBuilder.class);

    @Nonnull
    public static RoomInfo buildRoomInfo(@Nonnull Room ri) {
        logger.info("Room: '{}'",ri);
        return new RoomInfo(ri.getRid(),ri.getCinema().getCid(), ri.getCategory(),ri.getSeats());
    }

    @Nonnull
    public static CinemaInfo buildCinemaInfo(@Nonnull Cinema ci) {
        logger.info("Cinema: '{}'",ci);
        return new CinemaInfo(ci.getCid(),ci.getCname(), ci.getCountry(),ci.getCity(),ci.getRegion(),ci.getStreet(),ci.getRooms().stream().map(ModelBuilder::buildRoomInfo).collect(Collectors.toList()));
    }
}