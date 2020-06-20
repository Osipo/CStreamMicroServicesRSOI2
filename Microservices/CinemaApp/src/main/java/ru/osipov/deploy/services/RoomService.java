package ru.osipov.deploy.services;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.models.CreateRoom;
import ru.osipov.deploy.models.RoomInfo;

import java.net.URI;
import java.util.List;

@Transactional
public interface RoomService {

    @NonNull
    RoomInfo getRoomById(long id);

    @NonNull
    List<RoomInfo> getAllRooms();

    @NonNull
    List<RoomInfo> getByCategory(String cat);
    @NonNull
    List<RoomInfo> getBySeats(int seats);

    @NonNull
    List<RoomInfo> getByCid(long cid);
    @NonNull
    List<RoomInfo> getByCinemaName(String name);

    @NonNull
    List<RoomInfo> getBySeatNum(long num);
}
