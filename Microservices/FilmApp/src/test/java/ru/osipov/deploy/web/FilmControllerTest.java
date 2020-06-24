package ru.osipov.deploy.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.Null;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.configuration.jwt.JwtTokenProvider;
import ru.osipov.deploy.configuration.jwt.JwtTokenSupplier;
import ru.osipov.deploy.models.CreateFilm;
import ru.osipov.deploy.models.FilmInfo;
import ru.osipov.deploy.models.GenreInfo;
import ru.osipov.deploy.services.FilmService;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static ru.osipov.deploy.TestParams.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(FilmController.class)
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new GsonBuilder().create();
    private static final Logger logger = LoggerFactory.getLogger(FilmControllerTest.class);

    private static String token;

    @BeforeAll
    private static void initToken(){
        token = getToken(new JwtTokenProvider());
        assert token != null;
    }

    @MockBean
    FilmService fs;

    @Test
    void testAll() throws Exception {
        logger.info("testAll");
        List<FilmInfo> l = new ArrayList<>();
        List<GenreInfo> gs = new ArrayList<>();
        gs.add(new GenreInfo(2L,"Action",null));
        l.add(new FilmInfo(1L,PARAMS1[0],Short.parseShort(PARAMS1[1]),gs));
        l.add(new FilmInfo(2L,PARAMS2[0],Short.parseShort(PARAMS2[1]),null));
        l.add(new FilmInfo(3L,PARAMS3[0],Short.parseShort(PARAMS3[1]),null));
        when(fs.getAllFilms()).thenReturn(l);

        mockMvc.perform(get("/v1/films/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].rating").value(25))
                .andExpect(jsonPath("$[0].genres").isArray())
                .andExpect(jsonPath("$[0].genres[0].id").value(2L))
                .andExpect(jsonPath("$[0].genres[0].name").value("Action"))
                .andExpect(jsonPath("$[0].genres[0].remarks").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[1].rating").value(18))
                .andExpect(jsonPath("$[1].genres").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[2].id").value(3L))
                .andExpect(jsonPath("$[2].name").value(PARAMS3[0]))
                .andExpect(jsonPath("$[2].rating").value(16))
                .andExpect(jsonPath("$[2].genres").value(IsNull.nullValue()));
    }


    @Test
    void testByGid() throws Exception{
        logger.info("testByGid");
        List<FilmInfo> e = new ArrayList<>();
        when(fs.getFilmsByGName("Thriller")).thenReturn(e);
        mockMvc.perform(get("/v1/films/genre/Thriller").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/films/genre/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/v1/films/genre").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateGenre() throws Exception {
        logger.info("testUpdateGenre");
        List<FilmInfo> e = new ArrayList<>();
        List<FilmInfo> e2 = new ArrayList<>();
        e.add(new FilmInfo(12L,"IT",(short)23,null));
        when(fs.updateGenre(1L,"Null")).thenReturn(e);
        when(fs.getAllFilms()).thenReturn(e2);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/v1/films/1/genre/add/Null").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(12L))
                .andExpect(jsonPath("$[0].name").value("IT"))
                .andExpect(jsonPath("$[0].rating").value(23))
                .andExpect(jsonPath("$[0].genres").value(IsNull.nullValue()));
    }

    @Test
    void testById() throws Exception {
        logger.info("testById");
        FilmInfo f = new FilmInfo(100L,"MYST",(short)16,null);
        when(fs.getFilmById(100L)).thenReturn(f);
        doThrow(new IllegalStateException("Film not found.")).when(fs).getFilmById(0L);
        List<FilmInfo> e = new ArrayList<>();
        when(fs.getAllFilms()).thenReturn(e);
        mockMvc.perform(get("/v1/films/100").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.name").value("MYST"))
                .andExpect(jsonPath("$.rating").value(16))
                .andExpect(jsonPath("$.genres").value(IsNull.nullValue()));
        mockMvc.perform(get("/v1/films/0").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/v1/films").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/films/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void testByName() throws Exception {
        logger.info("testByName");
        final List<FilmInfo> emt = new ArrayList<>();
        final FilmInfo f = new FilmInfo(111L,"MYST",(short)100,null);
        when(fs.getAllFilms()).thenReturn(emt);
        when(fs.getByName("MYST")).thenReturn(f);
        doAnswer(new Answer<FilmInfo>() {
            @Override
            public FilmInfo answer(InvocationOnMock invocation) throws Throwable {
                return new FilmInfo(-2l,"IT",(short)-1,null);
            }
        }).when(fs).getByName("IT");

        mockMvc.perform(get("/v1/films/name/MYST").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(111L))
                .andExpect(jsonPath("$[0].name").value("MYST"))
                .andExpect(jsonPath("$[0].rating").value(100))
                .andExpect(jsonPath("$[0].genres").value(IsNull.nullValue()));

        mockMvc.perform(get("/v1/films/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/films").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/films/name/IT").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("Film with name = IT was not found."));
    }

    @Test
    void testByRating() throws Exception {
        logger.info("testByRating");
        final List<FilmInfo> emt = new ArrayList<>();
        final List<FilmInfo> r = new ArrayList<>();
        final List<GenreInfo> gs = new ArrayList<>();
        gs.add(new GenreInfo(24L,"Cartoon","Drawn."));
        r.add(new FilmInfo(55L,PARAMS1[0],Short.parseShort(PARAMS1[1]),gs));
        when(fs.getAllFilms()).thenReturn(emt);
        when(fs.getByRating((short)25)).thenReturn(r);

        mockMvc.perform(get("/v1/films?r=25").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(55L))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].rating").value(25))
                .andExpect(jsonPath("$[0].genres").isArray())
                .andExpect(jsonPath("$[0].genres[0].id").value(24L))
                .andExpect(jsonPath("$[0].genres[0].name").value("Cartoon"))
                .andExpect(jsonPath("$[0].genres[0].remarks").value("Drawn."));
        mockMvc.perform(get("/v1/films?r").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/films?r=").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void testUpdateRating() throws Exception {
        logger.info("testUpdateRating");
        FilmInfo r = new FilmInfo(555L,PARAMS1[0],(short)50,null);
        doReturn(r).when(fs).updateFilmRating(PARAMS1[0],(short)50);
        doThrow(new IllegalStateException("not exist : "+PARAMS3[0])).when(fs).updateFilmRating(PARAMS3[0],(short)25);
        doThrow(new IllegalStateException("Illegal rating value. Must be[0..100]: ")).when(fs).updateFilmRating(PARAMS1[0],(short)-23);
        doThrow(new IllegalStateException("Illegal rating value. Must be[0..100]: ")).when(fs).updateFilmRating(PARAMS1[0],(short)-1);
        doThrow(new IllegalStateException("Illegal rating value. Must be[0..100]: ")).when(fs).updateFilmRating(PARAMS1[0 ],(short)101);

        mockMvc.perform(post("/v1/films/update/"+PARAMS1[0]+"?r=50").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(555L))
                .andExpect(jsonPath("$.name").value(PARAMS1[0]))
                .andExpect(jsonPath("$.rating").value(50))
                .andExpect(jsonPath("$.genres").value(IsNull.nullValue()));

        mockMvc.perform(post("/v1/films/update/"+PARAMS1[0]+"?r=101").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/films/update/"+PARAMS1[0]+"?r=-23").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/films/update/"+PARAMS3[0]+"?r=25").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/v1/films/update/"+PARAMS1[0]+"?r=").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/films/update/"+PARAMS1[0]+"?r").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/films/update/"+PARAMS1[0]).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/v1/films/update/").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(405));
    }

    @Test
    void testUpdate() throws Exception {
        logger.info("testUpdate");
        ArrayList<GenreInfo> genres = new ArrayList<>();
        GenreInfo g = new GenreInfo(2L,"Horror",null);
        genres.add(g);
        CreateFilm req = new CreateFilm("IT 2",(short)33,genres);
        CreateFilm badreq = new CreateFilm("   ",(short)33,genres);
        CreateFilm badreq2 = new CreateFilm("Hell",(short)123,genres);
        CreateFilm badreq3 = new CreateFilm("Racer",(short)33,genres);


        FilmInfo f = new FilmInfo(333L,"IT",(short)25,genres);
        doReturn(f).when(fs).updateFilm(333L,req);
        doThrow(new IllegalStateException("Film was not found.")).when(fs).updateFilm(-1L,req);
        mockMvc.perform(patch("/v1/films/333").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(333L))
                .andExpect(jsonPath("$.name").value("IT"))
                .andExpect(jsonPath("$.rating").value(25))
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres[0].id").value(2L))
                .andExpect(jsonPath("$.genres[0].name").value("Horror"))
                .andExpect(jsonPath("$.genres[0].remarks").value(IsNull.nullValue()));
        mockMvc.perform(patch("/v1/films/-1").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/v1/films/23").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(patch("/v1/films/24").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq2)))
                .andExpect(status().isBadRequest());
//        mockMvc.perform(patch("/v1/films/333").header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq3)))
//                .andExpect(status().isBadRequest());

    }

    @Test
    void testDelete() throws Exception {
        logger.info("testDelete");
        FilmInfo r = new FilmInfo(333L,"IT",(short)25,null);
        doReturn(r).when(fs).deleteFilm(PARAMS1[0]);
        doThrow(new IllegalStateException("Film with name"+PARAMS3[0]+"was not found.")).when(fs).deleteFilm(PARAMS3[0]);


        mockMvc.perform(post("/v1/films/delete/"+PARAMS1[0]).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(333L))
                .andExpect(jsonPath("$.name").value(PARAMS1[0]))
                .andExpect(jsonPath("$.rating").value(25))
                .andExpect(jsonPath("$.genres").value(IsNull.nullValue()));

        mockMvc.perform(post("/v1/films/delete/"+PARAMS3[0]).header("Authorization","Basic "+token).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    protected static String getToken(JwtTokenSupplier supplier) {
        return supplier.getTokenForTests();
    }
}
