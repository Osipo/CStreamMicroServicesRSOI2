package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osipov.deploy.entities.QueueItemCinema;
import ru.osipov.deploy.entities.QueueItemSeance;

import java.util.List;

public interface QueueItemSeanceRepository extends JpaRepository<QueueItemSeance, Long> {
    List<QueueItemSeance> getByQid(Long qid);
}