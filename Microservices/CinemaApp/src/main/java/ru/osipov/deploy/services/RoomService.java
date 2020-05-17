package ru.osipov.deploy.services;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.models.RoomInfo;

import java.util.List;

@Transactional
public interface RoomService {
    @NonNull
    List<RoomInfo> getAllRooms();

    @NonNull
    List<RoomInfo> getByCategory(String cat);
    @NonNull
    List<RoomInfo> getBySeats(int seats);
}
