package ru.osipov.deploy.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.configuration.jwt.JwtTokenSupplier;
import ru.osipov.deploy.models.OrderInfo;
import ru.osipov.deploy.services.OrderService;
import org.hamcrest.core.IsNull;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new GsonBuilder().create();
    private static final Logger logger = LoggerFactory.getLogger(OrderControllerTest.class);

    private static String token;

    @BeforeAll
    static void initToken(){
        token = getToken(new JwtTokenProvider());
        assert token != null;
    }

    protected static String getToken(JwtTokenSupplier supplier) {
        return supplier.getTokenForTests();
    }

    @MockBean
    private OrderService serv;

    @Test
    public void testGetByUDate() throws Exception {
        LocalDate d = LocalDate.now();
        List<OrderInfo> res = new ArrayList<>();
        System.out.println(d.toString());
        when(serv.getByUpdatedDate(d)).thenReturn(res);
        mockMvc.perform(get("/v1/orders/?udate="+d.toString()).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
