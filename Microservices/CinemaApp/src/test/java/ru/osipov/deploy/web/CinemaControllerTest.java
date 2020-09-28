package ru.osipov.deploy.web;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.CreateCinema;
import ru.osipov.deploy.models.RoomInfo;
import ru.osipov.deploy.services.CinemaService;

import java.net.URI;
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
@WebMvcTest(CinemaController.class)
@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService serv;

    private static final Logger logger = LoggerFactory.getLogger(CinemaControllerTest.class);


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
        final List<CinemaInfo> cinemas = new ArrayList<>();
        final List<RoomInfo> rooms = new ArrayList<>();
        rooms.add(new RoomInfo(1l,11l,"Standard",100,1));
        rooms.add(new RoomInfo(2l,11l,"VIP",125,2));
        cinemas.add(new CinemaInfo(11l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
        cinemas.add(new CinemaInfo(12l,PARAMS2[0],PARAMS2[1],PARAMS2[2],PARAMS2[3],PARAMS2[4]));
        when(serv.getAllCinemas()).thenReturn(cinemas);

        mockMvc.perform(get("/v1/cinemas").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));
    }

    @Test
    void getById() throws Exception {
        logger.info("testById");
        CinemaInfo c = new CinemaInfo(10L,"MAX","USA","New York",null,"S");
        when(serv.getCinemaById(10L)).thenReturn(c);
        doThrow(new IllegalStateException("Film not found.")).when(serv).getCinemaById(0L);
        List<CinemaInfo> emt = new ArrayList<>();
        when(serv.getAllCinemas()).thenReturn(emt);
        mockMvc.perform(get("/v1/cinemas/10").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("MAX"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.street").value("S"));

        mockMvc.perform(get("/v1/cinemas/0").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/cinemas/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/cinemas").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByCity() throws Exception {
        logger.info("testByCity");
        final List<CinemaInfo> cinemas = new ArrayList<>();
        cinemas.add(new CinemaInfo(11l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
        final List<CinemaInfo> alls = new ArrayList<>();
        alls.add(new CinemaInfo(22l,PARAMS2[0],PARAMS2[1],PARAMS2[2],PARAMS2[3],PARAMS2[4]));

        when(serv.getByCity("Kazan")).thenReturn(cinemas);
        when(serv.getAllCinemas()).thenReturn(alls);

        mockMvc.perform(get("/v1/cinemas/city/Kazan").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

        //then parameter is null return all

        mockMvc.perform(get("/v1/cinemas/city/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(22l))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS2[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS2[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS2[4]));
    }

    @Test
    void getByCountry() throws Exception {
        logger.info("testByCountry");
        final List<CinemaInfo> cinemas = new ArrayList<>();
        cinemas.add(new CinemaInfo(12l,PARAMS2[0],PARAMS2[1],PARAMS2[2],PARAMS2[3],PARAMS2[4]));

        final List<CinemaInfo> am = new ArrayList<>();
        am.add(new CinemaInfo(99l,PARAMS4[0],PARAMS4[1],PARAMS4[2],PARAMS4[3],PARAMS4[4]));

        final List<CinemaInfo> alls = new ArrayList<>();
        when(serv.getAllCinemas()).thenReturn(alls);
        when(serv.getByCountry("USA")).thenReturn(am);
        when(serv.getByCountry("The Russian Federation")).thenReturn(cinemas);

        mockMvc.perform(get("/v1/cinemas/country/The Russian Federation").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(12l))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS2[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS2[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS2[4]));

        mockMvc.perform(get("/v1/cinemas/country/USA").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(99l))
                .andExpect(jsonPath("$[0].name").value(PARAMS4[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS4[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS4[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS4[4]));

        mockMvc.perform(get("/v1/cinemas/country/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByRegion() throws Exception {
        logger.info("testByRegion");
        final List<CinemaInfo> an = new ArrayList<>();
        an.add(new CinemaInfo(99l,PARAMS4[0],PARAMS4[1],PARAMS4[2],PARAMS4[3],PARAMS4[4]));
        an.add(new CinemaInfo(999l,PARAMS3[0],PARAMS3[1],PARAMS3[2],PARAMS3[3],PARAMS3[4]));
        final List<CinemaInfo> empt = new ArrayList<>();
        final List<CinemaInfo> reg = new ArrayList<>();
        reg.add(new CinemaInfo(1l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));

        when(serv.getByRegion(null)).thenReturn(an);//non null
        when(serv.getAllCinemas()).thenReturn(empt);
        when(serv.getByRegion("Moscovskyi")).thenReturn(reg);
        mockMvc.perform(get("/v1/cinemas/region/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(99l))
                .andExpect(jsonPath("$[0].name").value(PARAMS4[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS4[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS4[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS4[4]))
                .andExpect(jsonPath("$[1].id").value(999l))
                .andExpect(jsonPath("$[1].name").value(PARAMS3[0]))
                .andExpect(jsonPath("$[1].country").value(PARAMS3[1]))
                .andExpect(jsonPath("$[1].city").value(PARAMS3[2]))
                .andExpect(jsonPath("$[1].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].street").value(PARAMS3[4]));

        mockMvc.perform(get("/v1/cinemas/region/?all").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(99l))
                .andExpect(jsonPath("$[0].name").value(PARAMS4[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS4[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS4[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS4[4]))
                .andExpect(jsonPath("$[1].id").value(999l))
                .andExpect(jsonPath("$[1].name").value(PARAMS3[0]))
                .andExpect(jsonPath("$[1].country").value(PARAMS3[1]))
                .andExpect(jsonPath("$[1].city").value(PARAMS3[2]))
                .andExpect(jsonPath("$[1].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].street").value(PARAMS3[4]));

        mockMvc.perform(get("/v1/cinemas/region/?all=true").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());



        mockMvc.perform(get("/v1/cinemas/region/Moscovskyi").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

    }

    @Test
    void getByStreet() throws Exception {

        logger.info("testByStreet");
        final List<CinemaInfo> empt = new ArrayList<>();
        final List<CinemaInfo> str = new ArrayList<>();
        str.add(new CinemaInfo(1l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
        when(serv.getAllCinemas()).thenReturn(empt);
        when(serv.getByStreet("Korolenko")).thenReturn(str);
        when(serv.getByStreet(any(String.class))).thenReturn(str);
        when(serv.getByStreet("null")).thenReturn(empt);
        mockMvc.perform(get("/v1/cinemas/street/Korolenko").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

        mockMvc.perform(get("/v1/cinemas/street/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/cinemas/street/null").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByName() throws Exception {
        logger.info("testByName");
        final List<CinemaInfo> empt = new ArrayList<>();
        CinemaInfo ans = new CinemaInfo(1l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]);
        when(serv.getAllCinemas()).thenReturn(empt);
        when(serv.getByName("CMax")).thenReturn(ans);
        doAnswer(new Answer<CinemaInfo>() {
            @Override
            public CinemaInfo answer(InvocationOnMock invocation) throws Throwable {
                return new CinemaInfo(-2l,"","","","","");
            }
        }).when(serv).getByName("AKA47");

        mockMvc.perform(get("/v1/cinemas/name/CMax").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

        mockMvc.perform(get("/v1/cinemas/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/cinemas/name/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/v1/cinemas/name").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/v1/cinemas/name/AKA47").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("Cinema with name = AKA47 was not found."));
    }

    @Test
    void testUpdate() throws Exception {
        logger.info("testUpdate");
        CinemaInfo res = new CinemaInfo(1L,"Max","USA","LOS",null,"Street");
        CreateCinema req = new CreateCinema("Max","USA","LOS",null,"Street");
        CreateCinema badreq = new CreateCinema("Max","USA","LS",null,"");
        when(serv.updateCinema(1L,req)).thenReturn(res);
        doThrow(new IllegalStateException("not found.")).when(serv).updateCinema(-1L,req);

        mockMvc.perform(patch("/v1/cinemas/1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.city").value("LOS"))
                .andExpect(jsonPath("$.region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.street").value("Street"));

        mockMvc.perform(patch("/v1/cinemas/-1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/v1/cinemas/1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreate() throws Exception{
        logger.info("testCreate");
        CreateCinema req = new CreateCinema("Max","USA","LOS",null,"Street");
        CreateCinema badreq = new CreateCinema("Max","USA","LS",null,"");
        URI url = URI.create("/v1/cinemas/" + 1L);
        when(serv.createCinema(req)).thenReturn(url);
        doThrow(new IllegalStateException("not found.")).when(serv).createCinema(badreq);

        mockMvc.perform(post("/v1/cinemas/create").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(gson.toJson(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",url.toString()));

        mockMvc.perform(post("/v1/cinemas/create").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testDelete() throws Exception{
        logger.info("testDelete");
        CinemaInfo res = new CinemaInfo(1L,"Max","USA","LOS",null,"Street");
        when(serv.deleteCinema(1L)).thenReturn(res);
        doThrow(new IllegalStateException("not found.")).when(serv).deleteCinema(-1L);


        mockMvc.perform(post("/v1/cinemas/delete/1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.city").value("LOS"))
                .andExpect(jsonPath("$.region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.street").value("Street"));

        mockMvc.perform(post("/v1/cinemas/delete/-1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    protected static String getToken(JwtTokenSupplier supplier) {
        return supplier.getTokenForTests();
    }
}