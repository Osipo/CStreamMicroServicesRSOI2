package ru.osipov.deploy.services;

import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Film;
import ru.osipov.deploy.models.CreateFilm;
import ru.osipov.deploy.models.FilmInfo;

import javax.annotation.Nonnull;
import java.util.List;

@Transactional
public interface FilmService {
    @Nonnull
    List<FilmInfo> getAllFilms();

    @Nonnull
    FilmInfo getByName(@Nonnull String name);

    @Nonnull
    FilmInfo getFilmById(Long fid);

    @Nonnull
    List<FilmInfo> getFilmsByGName(String gname);


    @Nonnull
    List<FilmInfo> getByRating(Short rating);

    FilmInfo updateFilmRating(String fname, Short rating);

    @Nonnull
    List<FilmInfo> updateGenre(Long fid, String gname);

    FilmInfo deleteFilm(String name);

    FilmInfo updateFilm(Long id, @Nonnull CreateFilm request);

}
