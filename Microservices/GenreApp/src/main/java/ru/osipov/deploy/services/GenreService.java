package ru.osipov.deploy.services;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Genre;
import ru.osipov.deploy.models.CreateGenreR;
import ru.osipov.deploy.models.GenreInfo;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;

@Transactional
public interface GenreService {

    @Nonnull
    GenreInfo getGenreById(Long id);

    @Nonnull
    List<GenreInfo> getAllGenres();

    @Nonnull
    GenreInfo getByName(@Nonnull String name);

    GenreInfo updateGenre(@Nonnull String oldname, @Nonnull String nname);

    @Nonnull
    List<GenreInfo> getByRemarks(String remarks);

    URI createGenre(@Nonnull CreateGenreR request);

    GenreInfo updateGenre(Long id,@Nonnull CreateGenreR request);

    GenreInfo deleteGenre(@Nonnull Long id);
}
