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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    @Test
    void checkCreationTime(){
        LocalTime time = LocalTime.parse("11:23:12");
        List<Order> o = rep.findByCreatedTime(time);
        assert o != null && o.size() == 1;
        assert o.get(0).getCreatedTime().equals(time);
    }

    @Test
    void checkDate(){
        LocalDate date = LocalDate.parse("2030-11-12");
        List<Order> o = rep.findByCreatedAt(date);
        List<Order> o2 = rep.findByUpdatedAt(date);
        assert o != null && o2 != null;
        assert o.size() == 1 && o2.size() == 1;
        assert o.get(0).getCreatedAt().equals(date) && o2.get(0).getUpdatedAt().equals(date);
        assert o.get(0).equals(o2.get(0));
    }

    @Test
    void checkId(){
        Optional<Order> o = rep.findByOid(1l);
        assert o.isPresent();
        Order or = o.get();
        Optional<Order> o2 = rep.findById(1l);
        assert o2.isPresent();
        Order or2 = o2.get();
        assert or.equals(or2);
    }

    @Test
    void checkTotalSum(){
        List<Order> o = rep.findBySum(2000.0);
        assert o != null && o.size() == 1;
        Order or = o.get(0);
        assert or.getOid() == 3l && or.getStatus().equals("CONFIRMED");
        assert or.getItems() != null && or.getItems().size() == 2;
    }

    @Test
    void checkUid(){
        List<Order> o = rep.findByUid(1l);
        assert o != null && o.size() == 3;
        o = rep.findByUid(2l);
        assert o != null && o.size() == 0;
    }

    @Test
    void checkUidAndStatus(){
        List<Order> o = rep.findByStatusAndUid("CONFIRMED",1l);
        assert o != null && o.size() == 1;
        assert o.get(0).getSum().equals(2000.0);
    }

    @Test
    void checkFindByDiscount(){
        List<Order> o = rep.findByDiscount(0.1);
        assert o != null && o.size() == 1 && o.get(0).getOid().equals(3l);
    }
}
