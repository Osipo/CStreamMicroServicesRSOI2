package ru.osipov.deploy.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import ru.osipov.deploy.TestParams;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.configuration.jwt.JwtTokenSupplier;
import ru.osipov.deploy.models.CreateGenreR;
import ru.osipov.deploy.models.GenreInfo;
import ru.osipov.deploy.services.GenreService;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.osipov.deploy.TestParams.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GenreController.class)
@AutoConfigureMockMvc
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new GsonBuilder().create();
    private static final Logger logger = LoggerFactory.getLogger(GenreControllerTest.class);

    private static String token;

    @BeforeAll
    private static void initToken(){
        token = getToken(new JwtTokenProvider());
        assert token != null;
    }

    @MockBean
    GenreService gs;

    @Test
    void testAll() throws Exception {
        logger.info("testAll");
        final GenreInfo g1 = new GenreInfo(1L,PARAMS1[0], PARAMS1[1]);
        final GenreInfo g2 = new GenreInfo(2L,PARAMS2[0], PARAMS2[1]);
        final GenreInfo g3 = new GenreInfo(3L,PARAMS3[0], PARAMS3[1]);
        ArrayList<GenreInfo> l = new ArrayList<>();
        l.add(g1);l.add(g2);l.add(g3);
        when(gs.getAllGenres())
                .thenReturn(l);

        mockMvc.perform(get("/v1/genres").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS1[1]))
                .andExpect(jsonPath("$[1].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[1].remarks").value(PARAMS2[1]))
                .andExpect(jsonPath("$[2].name").value(PARAMS3[0]))
                .andExpect(jsonPath("$[2].remarks").value(PARAMS3[1]));
    }

    @Test
    void testById() throws Exception {
        logger.info("testById");
        final GenreInfo g = new GenreInfo(23L,"Horror","Scary story.");
        doReturn(g).when(gs).getGenreById(23L);
        doThrow(new IllegalStateException("Genre not found.")).when(gs).getGenreById(0L);
        final List<GenreInfo> emt = new ArrayList<>();
        when(gs.getAllGenres()).thenReturn(emt);
        mockMvc.perform(get("/v1/genres/23").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(23L))
                .andExpect(jsonPath("$.name").value("Horror"))
                .andExpect(jsonPath("$.remarks").value("Scary story."));

        mockMvc.perform(get("/v1/genres/0").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/genres/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/genres").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testByName() throws Exception {
        logger.info("testByName");
        final List<GenreInfo> emt = new ArrayList<>();
        final GenreInfo g = new GenreInfo(11L,PARAMS1[0],PARAMS1[1]);
        when(gs.getAllGenres()).thenReturn(emt);
        when(gs.getByName(PARAMS1[0])).thenReturn(g);
        mockMvc.perform(get("/v1/genres?name="+PARAMS1[0]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS1[1]));

        mockMvc.perform(get("/v1/genres/").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/genres").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        doAnswer(new Answer<GenreInfo>() {
            @Override
            public GenreInfo answer(InvocationOnMock invocation) throws Throwable {
                return new GenreInfo(-2l,"err","err");
            }
        }).when(gs).getByName("AKBAR!!!");

        mockMvc.perform(get("/v1/genres?name=AKBAR!!!").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("Genre with name = AKBAR!!! was not found."));
    }

    @Test
    void testByRemarks() throws Exception {
        logger.info("testByRemarks");
        final List<GenreInfo> emt = new ArrayList<>();
        final List<GenreInfo> an = new ArrayList<>();
        final List<GenreInfo> an2 = new ArrayList<>();
        an.add(new GenreInfo(11L,PARAMS2[0],PARAMS2[1]));
        an2.add(new GenreInfo(22L,PARAMS3[0],PARAMS3[1]));

        when(gs.getAllGenres()).thenReturn(emt);
        when(gs.getByRemarks(null)).thenReturn(an);
        when(gs.getByRemarks(PARAMS3[1])).thenReturn(an2);

        mockMvc.perform(get("/v1/genres/remarks?r="+PARAMS3[1]).header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(22L))
                .andExpect(jsonPath("$[0].name").value(PARAMS3[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS3[1]));

        mockMvc.perform(get("/v1/genres/remarks?r=&&all=").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS2[1]));

        mockMvc.perform(get("/v1/genres/remarks?all=true").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/genres/remarks?r=&&all=true").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/genres/remarks?r=").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS2[1]));
        mockMvc.perform(get("/v1/genres/remarks?all=").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS2[1]));
        mockMvc.perform(get("/v1/genres/remarks?all").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS2[1]));
        mockMvc.perform(get("/v1/genres/remarks").header("Authorization","Basic "+token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].remarks").value(PARAMS2[1]));
    }

    @Test
    void testUpdateName() throws Exception {
        logger.info("testUpdateName");
        GenreInfo g = new GenreInfo(11L,"Scary",PARAMS1[1]);
        doReturn(g).when(gs).updateGenre("Horror","Scary");
        doThrow(new IllegalStateException("not exist: "+PARAMS3[0])).when(gs).updateGenre(PARAMS3[0],"Action");
        doThrow(new IllegalStateException("Already have this name: "+PARAMS1[0])).when(gs).updateGenre(PARAMS1[0],PARAMS1[0]);

        //successful update
        mockMvc.perform(post("/v1/genres/update/"+PARAMS1[0]+"?newName=Scary").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(11L))
                .andExpect(jsonPath("$.name").value("Scary"))
                .andExpect(jsonPath("$.remarks").value(PARAMS1[1]));

        //bad paths.
        mockMvc.perform(post("/v1/genres/update/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(405));
        mockMvc.perform(post("/v1/genres/update/Horror").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/genres/update/Horror?newName=").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))//CHECK
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/genres/update/Horror?newName").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());


        //Bad operations (exceptions)
        //Update  genre that does not exist.
        mockMvc.perform(post("/v1/genres/update/"+PARAMS3[0]+"?newName=Action").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        //Update genre wtih non-unique name. (Names must be UNIQUE!!!)
        mockMvc.perform(post("/v1/genres/update/"+PARAMS1[0]+"?newName="+PARAMS1[0]).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception{
        logger.info("testUpdate");
        CreateGenreR badreq = new CreateGenreR("","");
        CreateGenreR req = new CreateGenreR("horror",null);
        GenreInfo g = new GenreInfo(1L,"scary","some");
        when(gs.updateGenre(1l,req)).thenReturn(g);
        doThrow(new IllegalStateException("not found.")).when(gs).updateGenre(-1l,req);
        mockMvc.perform(patch("/v1/genres/1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("scary"))
            .andExpect(jsonPath("$.remarks").value("some"));

        mockMvc.perform(patch("/v1/genres/1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq)))
            .andExpect(status().isBadRequest());
        mockMvc.perform(patch("/v1/genres/-1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception{
        logger.info("testDelete");
        GenreInfo g = new GenreInfo(11L,"Scary",PARAMS1[1]);
        doReturn(g).when(gs).deleteGenre(11L);
        doThrow(new IllegalStateException("NOT FOUND: "+32L)).when(gs).deleteGenre(32L);

        mockMvc.perform(post("/v1/genres/delete/"+11).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(11L))
                .andExpect(jsonPath("$.name").value("Scary"))
                .andExpect(jsonPath("$.remarks").value(PARAMS1[1]));

        mockMvc.perform(post("/v1/genres/delete/"+32).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(404));

        mockMvc.perform(post("/v1/genres/delete/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(500));

    }

    @Test
    void testCreate() throws Exception {
        CreateGenreR created = new CreateGenreR("Comedy","Funny story.");
        final URI url = URI.create("/v1/genres/5");
        when(gs.createGenre(eq(created))).thenReturn(url);
        mockMvc.perform(post("/v1/genres/create").header("Authorization","Basic "+token)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(gson.toJson(created)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",url.toString()));
    }

    protected static String getToken(JwtTokenSupplier supplier) {
        return supplier.getTokenForTests();
    }
}