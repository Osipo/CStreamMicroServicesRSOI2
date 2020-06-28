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
import ru.osipov.deploy.entities.OrderItem;
import ru.osipov.deploy.repositories.OrderItemRepository;
import ru.osipov.deploy.repositories.OrderRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-mysql-orders-test.sql","classpath:data-mysql-orders-test.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderItemRepoTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderItemRepository rep;

    @Test
    void injectedComponentsAreNotNull() {
        assert dataSource != null;
        assert jdbcTemplate != null;
        assert (entityManager) != null;
        assert rep != null;
    }

    @Test
    void checkFindByDiscount(){
        List<OrderItem> o = rep.findByDiscount(0.0);
        assert o != null && o.size() == 5;
    }

    @Test
    void checkFindByPrice(){
        List<OrderItem> o = rep.findByPrice(600.25);
        assert o != null && o.size() == 1;
        OrderItem it = o.get(0);
        assert it.getDiscount().equals(0.0);
        Order or = it.getOrder();
        assert or != null && or.getOid().equals(2l);

        o = rep.findByPrice(null);
        assert o != null && o.size() == 0;
    }

    @Test
    void checkPriceAndDiscount(){
        List<OrderItem> o = rep.findByDiscountAndPrice(0.0,900.50);
        assert o != null && o.size() == 1;
        OrderItem it = o.get(0);
        assert it.getPrice().equals(900.50) && it.getOrder() != null && it.getOrder().getOid().equals(2l);
        o = rep.findByDiscountAndPrice(null,900.50);
        assert o != null && o.size() == 0;
        o = rep.findByDiscountAndPrice(0.1,null);
        assert o != null && o.size() == 0;
        o = rep.findByDiscountAndPrice(null,null);
        assert o != null && o.size() == 0;
    }

    @Test
    void checkSid(){
        List<OrderItem> o = rep.findBySid(1l);
        assert o != null && o.size() == 2;
        o = rep.findBySid(null);
        assert o != null && o.size() == 0;
    }

    @Test
    void checkSeatId(){
        List<OrderItem> o = rep.findBySeatId(5l);
        assert o != null && o.size() == 1;
        OrderItem it = o.get(0);
        assert it.getPrice().equals(1000.0) && it.getDiscount().equals(0.1);
        assert it.getSid().equals(2l) && it.getOrder() != null && it.getOrder().getOid().equals(3l);

        o = rep.findBySeatId(null);
        assert o != null && o.size() == 0;
    }

    @Test
    void checkTotalSum(){
        List<OrderItem> o  = rep.findByTotalSum(2000.0);
        assert o != null && o.size() == 2;
    }
}
