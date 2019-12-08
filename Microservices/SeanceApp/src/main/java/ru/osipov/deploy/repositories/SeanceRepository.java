package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.SeancePK;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SeanceRepository extends JpaRepository<Seance, SeancePK> {
    List<Seance> findByFid(Long fid);
    List<Seance> findByCid(Long cid);
    Optional<Seance> findByFidAndCid(Long fid, Long cid);

    List<Seance> findAllByDate(LocalDate begining);
    List<Seance> findAllByDateBetween(LocalDate beginingStart, LocalDate beginingEnd);

    @Query(value = "select * from Seance a where a.begining <= :start",nativeQuery = true)
    List<Seance> findAllByDateBefore(@Param("start") LocalDate start);
}
