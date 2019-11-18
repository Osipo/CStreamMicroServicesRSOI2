package ru.osipov.deploy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.utils.Paginator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GatewayApp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPaginator {

    Paginator paginator;
    Logger logger = LoggerFactory.getLogger(TestPaginator.class);

    @BeforeAll
    void setPaginator(){
        paginator = new Paginator();
    }

    @AfterAll
    void removePaginator(){
        paginator = null;
    }

    @Test
    void startPaginator(){
        assertNotNull(paginator);
    }

    @Test
    void checkPaginatorAllInvalidArgs(){
        List<Integer> r = new ArrayList<>();
        r.add(1);r.add(2);r.add(3);r.add(4);r.add(5);
        List<Integer> ans = Paginator.getResult(r,6,2,6);
        assert ans.size() == 1;
        assert ans.get(0).equals(5);
    }

    @Test
    void checkPaginatorPages(){
        List<Integer> r = new ArrayList<>();
        r.add(1);r.add(2);r.add(3);r.add(4);r.add(5);
        List<Integer> ans = Paginator.getResult(r,6,1,3);
        List<Integer> ans2 = Paginator.getResult(r,-1,1,4);
        assert ans.size() == 1;
        assert ans.get(0).equals(3);
        assert ans2.size() == 1;
        assert ans2.get(0).equals(1);
    }

    @Test
    void checkPaginatorSize(){
        List<Integer> r = new ArrayList<>();
        r.add(5);r.add(4);r.add(3);r.add(2);r.add(1);
        List<Integer> ans = Paginator.getResult(r,2,4,2);
        assert ans.size() == 1;
        assert ans.get(0).equals(1);
        ans = Paginator.getResult(r,2,4,1);
        assert ans.size() == 4;
        assert ans.get(1).equals(4);
    }
    @Test
    void checkPageAndSize(){
        List<Integer> r = new ArrayList<>();
        r.add(5);r.add(4);r.add(3);r.add(2);r.add(1);
        List<Integer> ans = Paginator.getResult(r,2,1,2);
        assert ans.size() == 1;
        assert ans.get(0).equals(4);
        ans = Paginator.getResult(r,2,2,1);
        assert ans.size() == 2;
        assert ans.get(1).equals(4);
        ans = Paginator.getResult(r,2,4,2);
        assert ans.size() == 1;
        assert ans.get(0).equals(1);
        ans = Paginator.getResult(r,1,4,1);
        assert ans.size() == 4;
        assert ans.get(2).equals(3);
        ans = Paginator.getResult(r,3,2,3);
        assert ans.size() == 1;
        assert ans.get(0).equals(1);
    }
}
