package ru.osipov.deploy.services;

import lombok.NonNull;
import ru.osipov.deploy.models.ChangeSeatState;
import ru.osipov.deploy.models.CreateSeat;
import ru.osipov.deploy.models.SeatInfo;

import java.net.URI;
import java.util.List;

public interface CUDSeatService extends SeatService {
    @NonNull
    URI createSeat(long rid, CreateSeat req);

    @NonNull
    SeatInfo updateSeat(long id, CreateSeat data);

    @NonNull
    List<SeatInfo> setNewState(List<ChangeSeatState> selected);

    @NonNull
    SeatInfo deleteSeat(long id);
}
