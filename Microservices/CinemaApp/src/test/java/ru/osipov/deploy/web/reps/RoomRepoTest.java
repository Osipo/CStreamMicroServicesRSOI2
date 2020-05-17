package ru.osipov.deploy.web.reps;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.entities.Room;
import ru.osipov.deploy.repositories.CinemaRepository;
import ru.osipov.deploy.repositories.RoomRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-cinema-test.sql","classpath:data-mysql-cinema-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RoomRepoTest {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private RoomRepository rRep;

    @Test
    void injectedComponentsAreNotNull(){
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert rRep != null;
    }

    @Test
    void getExistingById(){
        Optional<Room> o1 = rRep.findByRid(1l);
        Optional<Room> o2 = rRep.findById(1l);
        assert o1.isPresent();
        assert o2.isPresent();
        assert o1.get().equals(o2.get());
    }
}
