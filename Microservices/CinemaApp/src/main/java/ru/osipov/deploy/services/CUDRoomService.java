package ru.osipov.deploy.services;

import lombok.NonNull;
import ru.osipov.deploy.models.CreateRoom;
import ru.osipov.deploy.models.RoomInfo;

import java.net.URI;

public interface CUDRoomService extends RoomService {
    @NonNull
    URI createRoom(long cid, CreateRoom req);

    @NonNull
    RoomInfo updateRoom(long id, CreateRoom data);

    @NonNull
    RoomInfo deleteRoom(long id);
}
