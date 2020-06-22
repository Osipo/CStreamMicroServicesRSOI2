package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.Seat;
import rx.internal.operators.OperatorTimeInterval;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    Optional<Seat> findBySid(long sid);
    List<Seat> findByNum(long num);
    List<Seat> findByState(String state);

    @Query("select s from Room r join r.seats s where r.rid = ?1")
    List<Seat> findByRoomId(long rid);
}
