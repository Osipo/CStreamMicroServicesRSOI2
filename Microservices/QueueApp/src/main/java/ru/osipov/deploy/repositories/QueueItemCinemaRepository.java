package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osipov.deploy.entities.QueueItemCinema;

public interface QueueItemCinemaRepository extends JpaRepository<QueueItemCinema, Long> {

}
