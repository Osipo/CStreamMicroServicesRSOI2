package ru.osipov.deploy.services;

import lombok.NonNull;
import ru.osipov.deploy.models.SeatInfo;

import java.util.List;

public interface SeatService {
    @NonNull
    List<SeatInfo> getAllSeats();

    @NonNull
    SeatInfo getSeatById(long sid);

    @NonNull
    List<SeatInfo> getSeatByState(String state);

    @NonNull
    List<SeatInfo> getSeatByNum(long number);

    @NonNull
    List<SeatInfo> getSeatsByRoom(long rid);

    @NonNull
    List<SeatInfo> getSeatsByRoomAndState(long rid,String state);
}
