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
import ru.osipov.deploy.repositories.CinemaRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-cinema-test.sql","classpath:data-mysql-cinema-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CinemaRepoTest {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private CinemaRepository cRep;

    @Test
    void injectedComponentsAreNotNull(){
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert cRep != null;
    }

    @Test
    void getExistingByName(){
        Optional<Cinema> o = cRep.findByCname("CMax");
        assert o.isPresent();
        assert o.get().getCname().equals("CMax");
    }

    @Test
    void getExistingByCity(){
        List<Cinema> c = cRep.findByCity("Moscow");
        assert c.size() != 0;
        assert c.get(0).getCname().equals("Karo");
    }

    @Test
    void getExistingByCountry(){
        List<Cinema> c = cRep.findByCountry("The Russian Federation");
        assert c.size() == 2;
    }

    @Test
    void getExistingByRegion(){
        List<Cinema> C = cRep.findByRegion("Moscovskyi");
        assert C.size() != 0;
        assert C.get(0).getCname().equals("CMax");
    }

    @Test
    void Nulls(){
        List<Cinema> C = cRep.findByRegion(null);
        List<Cinema> C1 = cRep.findByRegion("");
        assert C.size() > 0;
        assert C1.size() == 0;
    }

    @Test
    void getExistingByStreet(){
        List<Cinema> C = cRep.findByStreet("Venevskaya");
        assert C.size() != 0;
        assert C.get(0).getCname().equals("Karo");
    }

    @Test
    void getExistingById(){
        Optional<Cinema> o1 = cRep.findByCid(1l);
        Optional<Cinema> o2 = cRep.findById(1l);
        assert o1.isPresent();
        assert o2.isPresent();
        assert o1.get().equals(o2.get());
    }
}
