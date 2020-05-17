package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.osipov.deploy.entities.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findByRid(long rid);
    List<Room> findByCategory(String category);
    List<Room> findBySeats(int seats);

    @Query("select r from Cinema c join c.rooms r where c.cid = ?1")
    List<Room> findByCid(long cid);
}
