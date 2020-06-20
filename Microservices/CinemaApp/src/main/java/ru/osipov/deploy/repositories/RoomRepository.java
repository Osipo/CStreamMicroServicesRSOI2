package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.entities.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findByRid(long rid);
    List<Room> findByCategory(String category);
    List<Room> findBySeats(int seats);

    @Query("select r from Cinema c join c.rooms r where c.cid = ?1")
    List<Room> findByCid(long cid);
    @Query("select r from Cinema c join c.rooms r where c.cname = ?1")
    List<Room> findByCinemaName(String name);

    @Query("select r from Seat s join s.room r where s.num = ?1 ")
    List<Room> findBySeatNumber(long num);

}