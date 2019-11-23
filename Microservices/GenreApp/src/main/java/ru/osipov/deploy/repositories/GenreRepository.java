package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osipov.deploy.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByGid(Long gid);
    Optional<Genre> findByName(String name);
    List<Genre> findByRemarks(String remarks);
}