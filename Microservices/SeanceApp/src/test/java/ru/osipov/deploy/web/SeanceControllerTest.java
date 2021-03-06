package ru.osipov.deploy.web;

import com.google.gson.*;
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
import ru.osipov.deploy.models.CreateSeance;
import ru.osipov.deploy.models.SeanceInfo;
import ru.osipov.deploy.services.SeanceService;
import ru.osipov.deploy.web.utils.LocalDateAdapter;


import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.osipov.deploy.TestParams.*;
import static ru.osipov.deploy.TestParams.PARAMS1;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SeanceController.class)
@AutoConfigureMockMvc
public class SeanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeanceService serv;

    private Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class,new LocalDateAdapter()).create();


    private static final Logger logger = getLogger(SeanceControllerTest.class);


    @Test
    void testGetAll() throws Exception {
        logger.info("testAll");
        final List<SeanceInfo> seances = new ArrayList<>();
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS1[1]), LocalDate.parse(PARAMS1[2])));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS2[0]),Long.parseLong(PARAMS2[1]), LocalDate.parse(PARAMS2[2])));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS3[0]),Long.parseLong(PARAMS2[1]), LocalDate.parse(PARAMS3[2])));
        when(serv.getAllSeances()).thenReturn(seances);

        mockMvc.perform(get("/v1/seances")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS1[0])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[0].date").value(PARAMS1[2]))
                .andExpect(jsonPath("$[1].cid").value(Long.parseLong(PARAMS2[0])))
                .andExpect(jsonPath("$[1].fid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[1].date").value(PARAMS2[2]))
                .andExpect(jsonPath("$[2].cid").value(Long.parseLong(PARAMS3[0])))
                .andExpect(jsonPath("$[2].fid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[2].date").value(PARAMS3[2]));
    }

    @Test
    void testGetByCid() throws Exception {
        logger.info("testGetByCid");
        List<SeanceInfo> emt = new ArrayList<>();

        final List<SeanceInfo> seances = new ArrayList<>();
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS1[1]), LocalDate.parse(PARAMS1[2])));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS2[1]), LocalDate.parse(PARAMS2[2])));
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS2[1]), LocalDate.parse(PARAMS3[2])));
        when(serv.getAllSeances()).thenReturn(emt);
        when(serv.getSeancesInCinema(Long.parseLong(PARAMS1[0]))).thenReturn(seances);

        mockMvc.perform(get("/v1/seances/"+PARAMS1[0])
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS1[0])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[0].date").value(PARAMS1[2]))
                .andExpect(jsonPath("$[1].cid").value(Long.parseLong(PARAMS1[0])))
                .andExpect(jsonPath("$[1].fid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[1].date").value(PARAMS2[2]))
                .andExpect(jsonPath("$[2].cid").value(Long.parseLong(PARAMS1[0])))
                .andExpect(jsonPath("$[2].fid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[2].date").value(PARAMS3[2]));
        mockMvc.perform(get("/v1/seances/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByDate() throws Exception {
        logger.info("testByDate");
        final List<SeanceInfo> seances = new ArrayList<>();
        seances.add(new SeanceInfo(Long.parseLong(PARAMS1[0]),Long.parseLong(PARAMS1[1]),LocalDate.parse(PARAMS1[2])));
        final List<SeanceInfo> alls = new ArrayList<>();
        alls.add(new SeanceInfo(Long.parseLong(PARAMS2[0]),Long.parseLong(PARAMS2[1]),LocalDate.parse(PARAMS2[2])));

        when(serv.getSeancesByDate(PARAMS1[2])).thenReturn(seances);
        when(serv.getAllSeances()).thenReturn(alls);

        mockMvc.perform(get("/v1/seances?date="+PARAMS1[2])
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS1[0])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS1[1])))
                .andExpect(jsonPath("$[0].date").value(PARAMS1[2]));

        //then parameter is null return all

        mockMvc.perform(get("/v1/seances?date=").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS2[0])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[2]));

        mockMvc.perform(get("/v1/seances").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS2[0])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS2[1])))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[2]));
    }
    @Test
    void getByDateBetween() throws Exception {
          logger.info("testByDateBetween");
          final List<SeanceInfo> an = new ArrayList<>();
          an.add(new SeanceInfo(99l,99L,LocalDate.parse(PARAMS2[2])));
          an.add(new SeanceInfo(88L,88L,LocalDate.parse(PARAMS3[2])));
          an.add(new SeanceInfo(99l,77L,LocalDate.parse(PARAMS1[2])));
          final List<SeanceInfo> empt = new ArrayList<>();
          final List<SeanceInfo> ond = new ArrayList<>();
          ond.add(new SeanceInfo(Long.parseLong(PARAMS3[0]),Long.parseLong(PARAMS3[1]),LocalDate.parse(PARAMS2[2])));
          when(serv.getSeancesByDateBetween(PARAMS2[2],PARAMS1[2])).thenReturn(an);//BETWEEN
          when(serv.getAllSeances()).thenReturn(empt);
          when(serv.getSeancesByDate(PARAMS2[2])).thenReturn(ond);
          when(serv.getSeancesByDateBefore(PARAMS1[2])).thenReturn(an);

          mockMvc.perform(get("/v1/seances/date?d1="+PARAMS2[2]+"&&d2="+PARAMS1[2])
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(99L))
                .andExpect(jsonPath("$[0].fid").value(99L))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[2]))
                .andExpect(jsonPath("$[1].cid").value(88L))
                .andExpect(jsonPath("$[1].fid").value(88L))
                .andExpect(jsonPath("$[1].date").value(PARAMS3[2]))
                .andExpect(jsonPath("$[2].cid").value(99L))
                .andExpect(jsonPath("$[2].fid").value(77L))
                .andExpect(jsonPath("$[2].date").value(PARAMS1[2]));

        mockMvc.perform(get("/v1/seances/date?d1=&&d2=")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/seances/date")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/v1/seances/date?d1="+PARAMS2[2])
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(Long.parseLong(PARAMS3[0])))
                .andExpect(jsonPath("$[0].fid").value(Long.parseLong(PARAMS3[1])))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[2]));

        mockMvc.perform(get("/v1/seances/date?d2="+PARAMS1[2])
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(99L))
                .andExpect(jsonPath("$[0].fid").value(99L))
                .andExpect(jsonPath("$[0].date").value(PARAMS2[2]))
                .andExpect(jsonPath("$[1].cid").value(88L))
                .andExpect(jsonPath("$[1].fid").value(88L))
                .andExpect(jsonPath("$[1].date").value(PARAMS3[2]))
                .andExpect(jsonPath("$[2].cid").value(99L))
                .andExpect(jsonPath("$[2].fid").value(77L))
                .andExpect(jsonPath("$[2].date").value(PARAMS1[2]));
    }

    @Test
    void testUpdate() throws Exception {
        logger.info("testUpdate");
        CreateSeance req = new CreateSeance(2L,10L,LocalDate.parse("2019-12-13"));
        SeanceInfo r = new SeanceInfo(2L,10L,LocalDate.parse("2019-12-13"));
        when(serv.updateSeance(2L,10L,req)).thenReturn(r);
        doThrow(new IllegalStateException("NOT FOUND.")).when(serv).updateSeance(0L,2L,req);
        mockMvc.perform(patch("/v1/seances/2/10").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.cid").value(2L))
                .andExpect(jsonPath("$.fid").value(10L))
                .andExpect(jsonPath("$.date").value("2019-12-13"));
        mockMvc.perform(patch("/v1/seances/0/2").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isNotFound());

    }

    @Test
    void testDelete() throws Exception {
        logger.info("testDelete");
        doNothing().when(serv).deleteSeancesWithFilm(12L);
        doThrow(new IllegalStateException("NOT FOUND")).when(serv).deleteSeancesWithFilm(-1L);

        mockMvc.perform(post("/v1/seances/delete?fid=").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete?fid").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete?").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/v1/seances/delete?fid=12").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        logger.info("testCreate");
        CreateSeance created = new CreateSeance(2L,2L,LocalDate.now());
        final URI url = URI.create("/v1/genres/5");
        when(serv.createSeance(eq(created))).thenReturn(url);
        mockMvc.perform(post("/v1/seances/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(created)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",url.toString()));
    }
}
