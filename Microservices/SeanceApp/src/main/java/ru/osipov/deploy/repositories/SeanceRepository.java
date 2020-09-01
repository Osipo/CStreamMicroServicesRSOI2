package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.SeancePK;
import ru.osipov.deploy.entities.Ticket;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SeanceRepository extends JpaRepository<Seance, SeancePK> {
    List<Seance> findByFid(Long fid);

    @Query("select r from RoomsCinema c join Seance r where c.cid = ?1")
    List<Seance> findByCid(Long cid);

    @Query("select r from RoomsCinema c join Seance r on r.rid = c where c.cid = ?1 AND r.fid = ?2")
    Optional<Seance> findByFidAndCid(Long fid, Long cid);

    List<Seance> findAllByDate(LocalDate begining);

    @Query(value = "select * from Seance a where a.begining_date >= :beginingStart AND a.begining_date <= :beginingEnd", nativeQuery = true)
    List<Seance> findAllByDateBetween(@Param("beginingStart") LocalDate beginingStart,@Param("beginingEnd") LocalDate beginingEnd);

    @Query(value = "select * from Seance a where a.begining_date <= :start",nativeQuery = true)
    List<Seance> findAllByDateBefore(@Param("start") LocalDate start);
}
