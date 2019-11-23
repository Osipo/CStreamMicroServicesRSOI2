package ru.osipov.deploy.services;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;

@Service
public interface SeanceService {

    @NonNull
    public List<SeanceInfo> getAllSeances();

    @Nonnull
    public List<SeanceInfo> getSeancesInCinema(Long cid);

    @Nonnull
    public List<SeanceInfo> getSeancesByFilm(Long fid);

    @Nonnull
    public SeanceInfo getSeanceByFilmAndCinema(Long fid,Long cid);

    @NonNull
    public List<SeanceInfo> getSeancesByDate(String dateStr);

    @NonNull
    public List<SeanceInfo> getSeancesByDateBetween(String dateStart, String dateEnd);

    @NonNull
    public List<SeanceInfo> getSeancesByDateBefore(String dateStr);

    public void deleteSeancesWithFilm(Long fid);


    public SeanceInfo updateSeance(Long cid, Long fid, CreateSeance data);

    public URI createSeance(CreateSeance data);

}
