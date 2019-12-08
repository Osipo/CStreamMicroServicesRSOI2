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
import ru.osipov.deploy.utils.LinkedQueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GatewayApp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestLinkedQueue {

    LinkedQueue<String> queue;
    Logger logger = LoggerFactory.getLogger(TestLinkedQueue.class);

    @BeforeAll
    void setQueue(){
        queue = new LinkedQueue<String>();
    }

    @AfterAll
    void removeQueue(){
        queue = null;
    }

    @Test
    void startQueue(){
        assertNotNull(queue);
    }

    @Test
    void testAddAll(){
        logger.info("testAddAll");
        ArrayList<String> c = new ArrayList<>();
        c.add("t1");
        c.add("t2");
        c.add("t3");
        c.add("t4");
        c.add("t5");
        queue.addAll(c);
        for(String item : queue){
            logger.info("Item: "+item);
        }
        assert queue.toString().equals("^[ t1 t2 t3 t4 t5 ]$");
    }
}
