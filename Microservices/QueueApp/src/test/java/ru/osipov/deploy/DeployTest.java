package ru.osipov.deploy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.web.QueueController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {QueueApp.class})
//@ActiveProfiles("test")
public class DeployTest {

    @Autowired
    private QueueController controller;

    @Test
    public void testStarted(){
        assertNotNull(controller);
    }
}
