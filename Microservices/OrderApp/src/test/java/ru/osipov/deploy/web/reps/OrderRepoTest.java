package ru.osipov.deploy.web.reps;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.entities.Order;
import ru.osipov.deploy.repositories.OrderRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-orders-test.sql","classpath:data-mysql-orders-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderRepoTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderRepository rep;

    @Test
    void injectedComponentsAreNotNull() {
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert rep != null;
    }

    @Test
    void checkStatus(){
        List<Order> o = rep.findByStatus("SELECTED");
        assert o != null && o.size() == 2;
    }
}
