package ru.osipov.deploy.services;

import ru.osipov.deploy.entities.Genre;
import ru.osipov.deploy.models.GenreInfo;

import javax.annotation.Nonnull;

public class ModelBuilder {


    @Nonnull
    public static GenreInfo buildModel(@Nonnull Genre gi) {
        return new GenreInfo(gi.getGid(),gi.getName(), gi.getRemarks());
    }
}
