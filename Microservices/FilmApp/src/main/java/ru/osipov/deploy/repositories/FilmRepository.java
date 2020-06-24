package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.Film;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByFid(Long fid);
    Optional<Film> findByFname(String fname);
    List<Film>  findByRating(Short rating);
}
