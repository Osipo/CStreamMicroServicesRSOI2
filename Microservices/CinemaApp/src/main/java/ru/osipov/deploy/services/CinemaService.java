package ru.osipov.deploy.services;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateCinema;

import javax.annotation.Nonnull;
import java.util.List;

@Transactional
public interface CinemaService {
    @Nonnull
    List<CinemaInfo> getAllCinemas();

    @Nonnull
    CinemaInfo getCinemaById(Long cid);

    @Nonnull
    CinemaInfo getByName(String ciName);

    @Nonnull
    List<CinemaInfo> getByCountry(String country);

    @Nonnull
    List<CinemaInfo> getByRegion(String region);

    @Nonnull
    List<CinemaInfo> getByCity(String city);

    @Nonnull
    List<CinemaInfo> getByStreet(String street);

    CinemaInfo updateCinema(Long id, CreateCinema data);
}
