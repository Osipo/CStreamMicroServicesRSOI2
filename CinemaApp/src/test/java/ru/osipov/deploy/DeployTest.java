package ru.osipov.deploy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.web.CinemaController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {DeployApp.class})
//@ActiveProfiles("test")
public class DeployTest {

    @Autowired
    private CinemaController controller;

    @Test
    public void testStarted(){
        assertNotNull(controller);
    }
}
