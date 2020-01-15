package ru.osipov.deploy.repos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:create-data.sql","classpath:fetch-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UsersRepositoryTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository cRep;

    @Test
    void injectedComponentsAreNotNull() {
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert cRep != null;
    }
}