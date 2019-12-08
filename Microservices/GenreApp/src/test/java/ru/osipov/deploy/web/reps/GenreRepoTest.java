package ru.osipov.deploy.web.reps;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.entities.Genre;
import ru.osipov.deploy.repositories.GenreRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-genres-test.sql","classpath:data-mysql-genres-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GenreRepoTest {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private GenreRepository gRep;

    @Test
    void injectedComponentsAreNotNull(){
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert gRep != null;
    }
//
    @Test
    void getExistingByName(){
        Optional<Genre> o = gRep.findByName("Horror");
        assert o.isPresent();
    }

    @Test
    void NoNulls(){
        Optional<Genre> o = gRep.findByName(null);
        Optional<Genre> o2 = gRep.findByName("");
        assert o.isEmpty();
        assert o2.isEmpty();
    }
    @Test
    void getByNullRemarks(){
        List<Genre> G = gRep.findByRemarks(null);
        assert G.size() != 0;
        assert G.get(0).getName().equals("Action");
    }


    @Test
    void getById(){
        Optional<Genre> o1 = gRep.findByGid(21l);
        Optional<Genre> o2 = gRep.findById(21l);
        assert o1.isPresent() && o2.isPresent();
        assert o1.get().equals(o2.get());
    }
}
