package ru.osipov.deploy.web.reps;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.entities.Film;
import ru.osipov.deploy.repositories.FilmRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-films-test.sql","classpath:data-mysql-films-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilmRepoTest {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private FilmRepository fRep;

    @Test
    void injectedComponentsAreNotNull(){
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert fRep != null;
    }

    @Test
    void shouldBe3EntitiesBefore(){
        List<Film> films = fRep.findAll();
        System.out.println(films.size());
        assert films.size() != 0;
        assert films.size() == 3;
    }

    @Test
    void findExistingEntityByName(){
        Optional<Film> it = fRep.findByFname("MYST");
        assert it.isPresent();
    }
    @Test
    void findExistingEntityByRating(){
        List<Film> f = fRep.findByRating((short)22);
        assert f.size() != 0;
        assert f.get(0).getFname().equals("MYST");
        assert f.get(0).getGenres().size() == 1;
        assert f.get(0).getGenres().get(0).getName().equals("Cartoon");
    }

    @Test
    void getById(){
        Optional<Film> o1 = fRep.findByFid(10l);
        Optional<Film> o2 = fRep.findById(10l);
        assert o1.isPresent();
        assert o2.isPresent();
        assert o1.get().equals(o2.get());
    }

    @Test
    void expectNull(){
        Optional<Film> o1 = fRep.findByFid(null);
        Optional<Film> o = fRep.findByFname(null);
        List<Film> o2 = fRep.findByRating(null);
        assert o.isEmpty();
        assert o1.isEmpty();
        assert o2.size() == 0;
    }
}