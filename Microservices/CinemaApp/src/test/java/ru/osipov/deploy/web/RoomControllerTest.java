package ru.osipov.deploy.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNull;
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
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.configuration.jwt.JwtTokenSupplier;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.models.SeatInfo;
import ru.osipov.deploy.services.CUDRoomService;
import ru.osipov.deploy.services.CUDSeatService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.osipov.deploy.TestParams.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CUDRoomService serv;

    @MockBean
    private CUDSeatService seatServ;

    private static final Logger logger = LoggerFactory.getLogger(RoomControllerTest.class);


    private Gson gson = new GsonBuilder().serializeNulls().create();

    private static String token;

    @BeforeAll
    static void initToken(){
        token = getToken(new JwtTokenProvider());
        assert token != null;
    }

    @Test
    void testGetAll() throws Exception {
        logger.info("testGetAll");
        List<RoomInfo> res = new ArrayList<>();
        List<SeatInfo> st = new ArrayList<>();
        st.add(new SeatInfo(1l,10l,"NA",2l));
        res.add(new RoomInfo(1l,1l,"Standard",100,null));
        res.add(new RoomInfo(2l,1l,"V.I.P.",250,st));
        when(serv.getByCid(1l)).thenReturn(res);
        mockMvc.perform(get("/v1/cinemas/1/rooms/").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].rid").value(1l))
                .andExpect(jsonPath("$[0].cid").value(1l))
                .andExpect(jsonPath("$[0].category").value("Standard"))
                .andExpect(jsonPath("$[0].size").value(100))
                .andExpect(jsonPath("$[1].rid").value(2l))
                .andExpect(jsonPath("$[1].cid").value(1l))
                .andExpect(jsonPath("$[1].category").value("V.I.P."))
                .andExpect(jsonPath("$[1].size").value(250))
                .andExpect(jsonPath("$[0].seats").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].seats").isArray())
                .andExpect(jsonPath("$[1].seats[0].sid").value(1l))
                .andExpect(jsonPath("$[1].seats[0].rid").value(2l))
                .andExpect(jsonPath("$[1].seats[0].num").value(10l))
                .andExpect(jsonPath("$[1].seats[0].state").value("NA"));
    }

    @Test
    public void testById(){

    }

    protected static String getToken(JwtTokenSupplier supplier) {
        return supplier.getTokenForTests();
    }
}
