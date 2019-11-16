package ru.osipov.deploy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.web.ApiController;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GatewayApp.class})
public class TestStart {
    @Autowired
    private ApiController controller;


    @Test
    void testStarted(){
        assertNotNull(controller);
    }
}
