package ru.osipov.deploy.services;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.models.CinemaInfo;

import java.util.List;

@Transactional
public interface CinemaWithRoomsService extends CinemaService{

    @NonNull
    public List<CinemaInfo> findByCategory(String cat);
    @NonNull
    public List<CinemaInfo> findBySeats(int seats);
}
