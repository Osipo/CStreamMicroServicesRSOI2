package ru.osipov.deploy.web.reps;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.entities.Seance;
import ru.osipov.deploy.entities.SeancePK;
import ru.osipov.deploy.repositories.SeanceRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-seances-test.sql","classpath:data-mysql-seances-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class SeanceRepoTest {

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private SeanceRepository sRep;

    @Test
    void injectedComponentsAreNotNull(){
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert sRep != null;
    }

    @Test
    void findByDate() {
        List<Seance> r = sRep.findAllByDate(LocalDate.parse("2030-12-05"));//CHECK
        assert r != null;
        assert r.size() > 0;
    }

    @Test
    void findByDateBetween(){
        List<Seance> r = sRep.findAllByDateBetween(LocalDate.parse("2030-12-01"),LocalDate.parse("2030-12-05"));
        assert r != null;
        assert r.size() == 2;
    }

    @Test
    void findByDateBefore(){
        List<Seance> r = sRep.findAllByDateBefore(LocalDate.parse("2030-12-03"));
        assert r != null;
        assert r.size() == 3;
        System.out.println(r.get(0).getDate().toString());
        assert r.get(0).getDate().toString().equals("2030-12-03");
        assert r.get(0).getRid() != null && r.get(0).getRid().getRid() == 1L
                && r.get(0).getRid().getCid() == 2L;
        assert r.get(0).getTickets() != null && r.get(0).getTickets().size() == 1;
        assert r.get(0).getTickets().stream().collect(Collectors.toList()).get(0).getPrice() == 150.25D;
    }

    @Test
    void findById(){
        Optional<Seance> o2 = sRep.findByFidAndCid(10l,2l);
        //assert o1.isPresent();
        assert o2.isPresent();
        //assert o1.get().equals(o2.get());
    }

}
