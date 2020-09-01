package ru.osipov.deploy.web;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.configuration.jwt.JwtTokenSupplier;
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.services.SeanceService;
import ru.osipov.deploy.web.utils.LocalTimeAdapter;
import ru.osipov.deploy.web.utils.LocalDateAdapter;


import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.osipov.deploy.TestParams.*;
import static ru.osipov.deploy.TestParams.PARAMS1;

//All tests are failed due authorizatiion.
//TODO: Make a request to get token before all tests.
@ExtendWith(SpringExtension.class)
@WebMvcTest(SeanceController.class)
@AutoConfigureMockMvc
public class SeanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeanceService serv;

    private Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class,new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class,new LocalTimeAdapter()).create();


    private static final Logger logger = getLogger(SeanceControllerTest.class);

    private static String token;

    @BeforeAll
    private static void initToken(){
        token = getToken(new JwtTokenProvider());
        assert token != null;
    }

    @Test
    void testGetAll() throws Exception {
        logger.info("testAll");
        final List<SeanceInfo> seances = new ArrayList<>();
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS1[1]),Long.parseLong(PARAMS1[2]),Long.parseLong(PARAMS1[3]), LocalDate.parse(PARAMS1[4]), null));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS2[0]),Long.parseLong(PARAMS2[1]),Long.parseLong(PARAMS2[2]),Long.parseLong(PARAMS2[3]), LocalDate.parse(PARAMS2[4]), null));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS3[0]),Long.parseLong(PARAMS3[1]),Long.parseLong(PARAMS3[2]),Long.parseLong(PARAMS2[3]), LocalDate.parse(PARAMS3[4]), null));
        when(serv.getAllSeances()).thenReturn(seances);
        mockMvc.perform(get("/v1/seances").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS1[3])))
                .andExpect(jsonPath("$[0].date").value(PARAMS1[4]))
                .andExpect(jsonPath("$[1].cid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[1].fid").value(Long.parseLong(PARAMS2[3])))
                .andExpect(jsonPath("$[1].date").value(PARAMS2[4]))
                .andExpect(jsonPath("$[2].cid").value(Long.parseLong(PARAMS3[1])))
                .andExpect(jsonPath("$[2].fid").value(Long.parseLong(PARAMS2[3])))
                .andExpect(jsonPath("$[2].date").value(PARAMS3[4]));
    }

    @Test
    void testGetByCid() throws Exception {
        logger.info("testGetByCid");
        List<SeanceInfo> emt = new ArrayList<>();

        final List<SeanceInfo> seances = new ArrayList<>();
        LocalTime lt = LocalTime.now();
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS1[1]),Long.parseLong(PARAMS1[2]),Long.parseLong(PARAMS1[3]), LocalDate.parse(PARAMS1[4]), lt));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS2[0]),Long.parseLong(PARAMS2[1]),Long.parseLong(PARAMS2[2]),Long.parseLong(PARAMS2[3]), LocalDate.parse(PARAMS2[4]), lt));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS3[0]),Long.parseLong(PARAMS3[1]),Long.parseLong(PARAMS3[2]),Long.parseLong(PARAMS2[3]), LocalDate.parse(PARAMS3[4]), lt));
        when(serv.getAllSeances()).thenReturn(emt);
        when(serv.getSeancesInCinema(Long.parseLong(PARAMS1[1]))).thenReturn(seances);

        mockMvc.perform(get("/v1/seances/"+PARAMS1[1]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS1[3])))
                .andExpect(jsonPath("$[0].date").value(PARAMS1[4]))
                .andExpect(jsonPath("$[1].cid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[1].fid").value(Long.parseLong(PARAMS2[3])))
                .andExpect(jsonPath("$[1].date").value(PARAMS2[4]))
                .andExpect(jsonPath("$[2].cid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[2].fid").value(Long.parseLong(PARAMS2[3])))
                .andExpect(jsonPath("$[2].date").value(PARAMS3[4]));
        mockMvc.perform(get("/v1/seances/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByDate() throws Exception {
        logger.info("testByDate");
        final List<SeanceInfo> seances = new ArrayList<>();
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS1[1]),Long.parseLong(PARAMS1[2]),Long.parseLong(PARAMS1[3]), LocalDate.parse(PARAMS1[4]), null));
        final List<SeanceInfo> alls = new ArrayList<>();
        alls.add(new SeanceInfo(Long.parseLong(PARAMS2[0]),Long.parseLong(PARAMS2[1]),Long.parseLong(PARAMS2[2]),Long.parseLong(PARAMS2[3]), LocalDate.parse(PARAMS2[4]), null));

        when(serv.getSeancesByDate(PARAMS1[4])).thenReturn(seances);
        when(serv.getAllSeances()).thenReturn(alls);

        mockMvc.perform(get("/v1/seances?date="+PARAMS1[4]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS1[3])))
                .andExpect(jsonPath("$[0].date").value(PARAMS1[4]));

        //then parameter is null return all

        mockMvc.perform(get("/v1/seances?date=").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS2[3])))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[4]));

        mockMvc.perform(get("/v1/seances").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS2[3])))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[4]));
    }
    @Test
    void getByDateBetween() throws Exception {
          logger.info("testByDateBetween");
          final List<SeanceInfo> an = new ArrayList<>();
            an.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),99L,Long.parseLong(PARAMS1[2]),99L, LocalDate.parse(PARAMS2[4]), null));
            an.add(new SeanceInfo(Long.parseLong(PARAMS2[0]),88L,Long.parseLong(PARAMS2[2]),88L, LocalDate.parse(PARAMS3[4]), null));
            an.add(new SeanceInfo(Long.parseLong(PARAMS3[0]),99L,Long.parseLong(PARAMS3[2]),77L, LocalDate.parse(PARAMS1[4]), null));
          final List<SeanceInfo> empt = new ArrayList<>();
          final List<SeanceInfo> ond = new ArrayList<>();
          ond.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS3[1]),Long.parseLong(PARAMS1[2]),Long.parseLong(PARAMS3[3]), LocalDate.parse(PARAMS2[4]), null));
          when(serv.getSeancesByDateBetween(PARAMS2[4],PARAMS1[4])).thenReturn(an);//BETWEEN
          when(serv.getAllSeances()).thenReturn(empt);
          when(serv.getSeancesByDate(PARAMS2[4])).thenReturn(ond);
          when(serv.getSeancesByDateBefore(PARAMS1[4])).thenReturn(an);

          mockMvc.perform(get("/v1/seances/date?d1="+PARAMS2[4]+"&&d2="+PARAMS1[4]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(99L))
                .andExpect(jsonPath("$[0].fid").value(99L))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[4]))
                .andExpect(jsonPath("$[1].cid").value(88L))
                .andExpect(jsonPath("$[1].fid").value(88L))
                .andExpect(jsonPath("$[1].date").value(PARAMS3[4]))
                .andExpect(jsonPath("$[2].cid").value(99L))
                .andExpect(jsonPath("$[2].fid").value(77L))
                .andExpect(jsonPath("$[2].date").value(PARAMS1[4]));

        mockMvc.perform(get("/v1/seances/date?d1=&&d2=").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/seances/date").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/v1/seances/date?d1="+PARAMS2[4]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS3[1])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS3[3])))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[4]));

        mockMvc.perform(get("/v1/seances/date?d2="+PARAMS1[4]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(99L))
                .andExpect(jsonPath("$[0].fid").value(99L))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[4]))
                .andExpect(jsonPath("$[1].cid").value(88L))
                .andExpect(jsonPath("$[1].fid").value(88L))
                .andExpect(jsonPath("$[1].date").value(PARAMS3[4]))
                .andExpect(jsonPath("$[2].cid").value(99L))
                .andExpect(jsonPath("$[2].fid").value(77L))
                .andExpect(jsonPath("$[2].date").value(PARAMS1[4]));
    }

    @Test
    void testUpdate() throws Exception {
        logger.info("testUpdate");
        LocalTime lt = LocalTime.parse("15:00:02.13");//now() produce difference (break equals())
        CreateSeance req = new CreateSeance(2L,10L,1L,LocalDate.parse("2019-12-13"),lt);
        SeanceInfo r = new SeanceInfo(2L,2L,1L,10L,LocalDate.parse("2019-12-13"),lt);
        when(serv.updateSeance(2L,10L,req)).thenReturn(r);
        doThrow(new IllegalStateException("NOT FOUND.")).when(serv).updateSeance(0L,2L,req);
        mockMvc.perform(patch("/v1/seances/2/10").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.cid").value(2L))
                .andExpect(jsonPath("$.fid").value(10L))
                .andExpect(jsonPath("$.date").value("2019-12-13"));
        mockMvc.perform(patch("/v1/seances/0/2").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isNotFound());

    }

    @Test
    void testDelete() throws Exception {
        logger.info("testDelete");
        doNothing().when(serv).deleteSeancesWithFilm(12L);
        doThrow(new IllegalStateException("NOT FOUND")).when(serv).deleteSeancesWithFilm(-1L);

        mockMvc.perform(post("/v1/seances/delete?fid=").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete?fid").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete?").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete?fid=12").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        logger.info("testCreate");
        LocalTime lt = LocalTime.parse("15:00:00.12");
        CreateSeance created = new CreateSeance(2L,2L,1L,LocalDate.now(),lt);
        final URI url = URI.create("/v1/seances/5");
        when(serv.createSeance(created)).thenReturn(url);
        mockMvc.perform(post("/v1/seances/create").header("Authorization","Basic "+token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(created)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",url.toString()));
    }

    protected static String getToken(JwtTokenSupplier supplier) {
        return supplier.getTokenForTests();
    }
}
