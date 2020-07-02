package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osipov.deploy.entities.RoomsCinema;

import java.util.List;
import java.util.Optional;

public interface RoomsCinemaRepository extends JpaRepository<RoomsCinema, Long> {
    List<RoomsCinema> findByCid(Long cid);
    Optional<RoomsCinema> findByRid(Long rid);
}
