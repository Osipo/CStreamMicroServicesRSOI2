package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.Cinema;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByCid(Long cid);
    Optional<Cinema> findByCname(String cname);
    List<Cinema> findByCity(String city);
    List<Cinema> findByRegion(String region);
    List<Cinema> findByStreet(String street);
    List<Cinema> findByCountry(String country);
}
