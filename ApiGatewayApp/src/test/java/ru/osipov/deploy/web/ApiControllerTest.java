package ru.osipov.deploy.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.osipov.deploy.errors.ApiException;
import ru.osipov.deploy.models.*;
import ru.osipov.deploy.services.WebCinemaService;
import ru.osipov.deploy.services.WebFilmService;
import ru.osipov.deploy.services.WebGenreService;
import ru.osipov.deploy.services.WebSeanceService;
import ru.osipov.deploy.utils.LocalDateAdapter;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiController.class)
@AutoConfigureMockMvc
public class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson2 = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    private Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    private static final Logger logger = LoggerFactory.getLogger(ApiControllerTest.class);

    @MockBean
    WebGenreService gs;

    @MockBean
    WebFilmService fs;

    @MockBean
    WebCinemaService cs;

    @MockBean
    WebSeanceService ss;

    @Test
    void testGetAllFilms() throws Exception {
        logger.info("testGetAllFilms");
        FilmInfo[] e = new FilmInfo[0];
        FilmInfo[] f = new FilmInfo[1];
        List<FilmInfo> fr = new ArrayList<>();
        fr.add(new FilmInfo(1L,"IT",(short)16,21L));
        when(fs.getAll()).thenReturn(e);
        when(fs.getById(1L)).thenReturn(fr.toArray(f)[0]);
        when(fs.getByRating((short)16)).thenReturn(fr.toArray(f));

        mockMvc.perform(get("/v1/api/films?r=16").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("IT"))
                .andExpect(jsonPath("$[0].rating").value(16))
                .andExpect(jsonPath("$[0].genre").value(IsNull.nullValue()));

        mockMvc.perform(get("/v1/api/films/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("IT"))
                .andExpect(jsonPath("$.rating").value(16))
                .andExpect(jsonPath("$.genre").value(IsNull.nullValue()));


        mockMvc.perform(get("/v1/api/films/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/api/films").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/api/films?r=").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/api/films?r").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetFilmsByGid() throws Exception {
        logger.info("testGetFilmsByGid");
        FilmInfo[] e = new FilmInfo[0];
        FilmInfo[] f = new FilmInfo[1];
        List<FilmInfo> fr = new ArrayList<>();
        fr.add(new FilmInfo(1L,"IT",(short)16,21L));
        GenreInfo genre = new GenreInfo(21L,"Horror","Scary story.");
        when(fs.getByGid(21L)).thenReturn(fr.toArray(f));
        when(gs.getById(21L)).thenReturn(genre);
        mockMvc.perform(get("/v1/api/films/genre/21").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("IT"))
                .andExpect(jsonPath("$[0].rating").value(16))
                .andExpect(jsonPath("$[0].genre").exists())
                .andExpect(jsonPath("$[0].genre.id").value(21L))
                .andExpect(jsonPath("$[0].genre.name").value("Horror"))
                .andExpect(jsonPath("$[0].genre.remarks").value("Scary story."));

        mockMvc.perform(get("/v1/api/films/genre").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.ex").value("org.springframework.web.method.annotation.MethodArgumentTypeMismatchException"));
        mockMvc.perform(get("/v1/api/films/genre/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.ex").value("org.springframework.web.bind.MissingPathVariableException"));
    }

    @Test
    void testGetById() throws Exception {
        logger.info("testGetById");
        FilmInfo[] e = new FilmInfo[0];
        FilmInfo f = new FilmInfo(1L,"IT",(short)16,21L);
        when(fs.getById(1L)).thenReturn(f);
        when(fs.getAll()).thenReturn(e);
        mockMvc.perform(get("/v1/api/films/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("IT"))
                .andExpect(jsonPath("$.rating").value(16))
                .andExpect(jsonPath("$.genre").value(IsNull.nullValue()));
        mockMvc.perform(get("/v1/api/films").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/api/films/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testUpdateFilm() throws Exception {
        logger.info("testUpdateFilm");
        FilmInfo res = new FilmInfo(12L,"UPDATED",(short)55,10L);
        GenreInfo g = new GenreInfo(10L,"Action",null);
        CreateFilm req = new CreateFilm("UPDATED",(short)55,10L);
        CreateFilm badreq = new CreateFilm("BAD",(short)105,10L);

        when(gs.getById(10L)).thenReturn(g);
        when(fs.updateFilm(12L,req)).thenReturn(res);

        mockMvc.perform(patch("/v1/api/films/12").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(12L))
                .andExpect(jsonPath("$.name").value("UPDATED"))
                .andExpect(jsonPath("$.rating").value(55))
                .andExpect(jsonPath("$.genre").exists())
                .andExpect(jsonPath("$.genre.id").value(10L))
                .andExpect(jsonPath("$.genre.name").value("Action"))
                .andExpect(jsonPath("$.genre.remarks").value(IsNull.nullValue()));
        mockMvc.perform(patch("/v1/api/films/12").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(badreq)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(patch("/v1/api/films/").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().is(405));
        mockMvc.perform(patch("/v1/api/films").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(req)))
                .andExpect(status().is(405));
    }

    @Test
    void testGenresAll() throws Exception {
        logger.info("testGenresAll");
        GenreInfo[] g = new GenreInfo[0];
        when(gs.getAll()).thenReturn(g);
        mockMvc.perform(get("/v1/api/genres/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/api/genres").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGenresById() throws Exception {
        GenreInfo[] g = new GenreInfo[0];
        when(gs.getAll()).thenReturn(g);
        GenreInfo g2 = new GenreInfo(1L,"GET","");
        when(gs.getById(1L)).thenReturn(g2);
        mockMvc.perform(get("/v1/api/genres/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("GET"))
                .andExpect(jsonPath("$.remarks").value(""));
        mockMvc.perform(get("/v1/api/genres/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deleteGenre() throws Exception {
        GenreInfo g = new GenreInfo(1L,"Horror",null);
        when(gs.delete(1L)).thenReturn(g);
        doThrow(new ApiException("Not found",new IllegalStateException("original"),404,null,"NOT FOUND",null,null))
                .when(gs).delete(3L);

        mockMvc.perform(post("/v1/api/genres/delete/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Horror"))
                .andExpect(jsonPath("$.remarks").value(IsNull.nullValue()));
        mockMvc.perform(post("/v1/api/genres/delete/3").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.reason").value("NOT FOUND"))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.ex").value(IsNull.nullValue()));
        mockMvc.perform(post("/v1/api/genres/delete").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(405));
    }

    @Test
    void testCreateGenre() throws Exception {
        logger.info("testCreateGenre");
        URI url = URI.create("http://localhost:8089/path/123");
        Long id = Long.parseLong(url.getPath().substring(url.getPath().lastIndexOf("/") + 1));
        CreateGenreR data = new CreateGenreR("New","Genre");
        CreateGenreR bad = new CreateGenreR(" ",null);
        when(gs.createGenre(data)).thenReturn(url);
        mockMvc.perform(post("/v1/api/genres/create").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson2.toJson(data)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(post("/v1/api/genres/create").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson2.toJson(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateFilmGenre() throws Exception {
        logger.info("testUpdateFilmGenre");
        FilmInfo[] f1 = new FilmInfo[1];
        FilmInfo[] e = new FilmInfo[0];
        GenreInfo g1 = new GenreInfo(10L,"Action","Last.");
        f1[0] = new FilmInfo(1L,"IT",(short)18,10L);
        when(gs.getById(10L)).thenReturn(g1);
        when(fs.changeGenre(9L,10L)).thenReturn(f1);
        when(fs.changeGenre(8L,10L)).thenReturn(e);
        NewGenre ng = new NewGenre(10L);
        NewGenre bad = new NewGenre(-3L);
        mockMvc.perform(post("/v1/api/films/genre/9").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson2.toJson(ng)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("IT"))
                .andExpect(jsonPath("$[0].rating").value(18))
                .andExpect(jsonPath("$[0].genre").exists())
                .andExpect(jsonPath("$[0].genre.id").value(10L))
                .andExpect(jsonPath("$[0].genre.name").value("Action"))
                .andExpect(jsonPath("$[0].genre.remarks").value("Last."));

        mockMvc.perform(post("/v1/api/films/genre/8").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson2.toJson(ng)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(post("/v1/api/films/genre/8").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson2.toJson(bad)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testGetAllCinemas() throws Exception {
        logger.info("testGetAllCinemas");
        CinemaInfo[] c = new CinemaInfo[0];
        CinemaInfo r = new CinemaInfo(1L,"MAX","RU","CI",null,"Str");
        when(cs.getAll()).thenReturn(c);
        when(cs.getById(1L)).thenReturn(r);
        mockMvc.perform(get("/v1/api/cinemas/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/api/cinemas").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/v1/api/cinemas/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("MAX"))
                .andExpect(jsonPath("$.country").value("RU"))
                .andExpect(jsonPath("$.city").value("CI"))
                .andExpect(jsonPath("$.region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.street").value("Str"));
    }

    @Test
    void testGetCinemaSeances() throws Exception {
        logger.info("testGetCinemaSeances");
        CinemaInfo r = new CinemaInfo(1L,"MAX","RU","CI",null,"Str");
        when(cs.getById(1L)).thenReturn(r);
        SeanceInfo s = new SeanceInfo(1L,12L,LocalDate.now());
        when(ss.getByCid(1L)).thenReturn(new SeanceInfo[]{s});
        doThrow(new ApiException("cinema null",new IllegalStateException("origin"),404,null,"CINEMA 404",null,null))
                .when(cs).getById(-1L);

        mockMvc.perform(get("/v1/api/cinemas/1/seances").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.cinema").exists())
                .andExpect(jsonPath("$.cinema.id").value(1L))
                .andExpect(jsonPath("$.cinema.name").value("MAX"))
                .andExpect(jsonPath("$.cinema.country").value("RU"))
                .andExpect(jsonPath("$.cinema.city").value("CI"))
                .andExpect(jsonPath("$.cinema.region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.cinema.street").value("Str"))
                .andExpect(jsonPath("$.seances").isArray())
                .andExpect(jsonPath("$.seances[0].cid").value(1L))
                .andExpect(jsonPath("$.seances[0].fid").value(12L))
                .andExpect(jsonPath("$.seances[0].date").value(s.getDate().toString()));

        mockMvc.perform(get("/v1/api/cinemas/-1/seances").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.reason").value("CINEMA 404"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void testGetSeance() throws Exception {
        logger.info("testGetSeance");
        CinemaInfo r = new CinemaInfo(1L,"MAX","RU","CI",null,"Str");
        FilmInfo f = new FilmInfo(2L,"IT",(short)18,3L);
        GenreInfo g = new GenreInfo(3L,"Horror","Scary.");
        SeanceInfo s = new SeanceInfo(1L,2L,LocalDate.now());
        when(fs.getById(2L)).thenReturn(f);
        when(gs.getById(3L)).thenReturn(g);
        when(ss.getByCidFid(1L,2L)).thenReturn(s);
        when(cs.getById(1L)).thenReturn(r);
        mockMvc.perform(get("/v1/api/cinemas/1/seances/2").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.cinema").value(r.getName()))
                .andExpect(jsonPath("$.location").value(r.getCountry()+"  "+r.getCity()))
                .andExpect(jsonPath("$.film").value(f.getName()))
                .andExpect(jsonPath("$.genre").value(g.getName()))
                .andExpect(jsonPath("$.rating").value(18))
                .andExpect(jsonPath("$.date").value(s.getDate().toString()));
    }

    @Test
    void testUpdateCinema() throws Exception {
        logger.info("testUpdateCinema");
        CinemaInfo r = new CinemaInfo(2L,"MAX","RUS","CITY",null,"Street");
        CreateCinema data = new CreateCinema("MAX","RUS","CITY",null,"Street",null);
        LocalDate date = LocalDate.now();
        CreateCinema data2 = new CreateCinema("MAX","RUS","CITY",null,"Street",new CreateSeance[]{new CreateSeance(2L,11L,date)});
        CreateCinema bad = new CreateCinema("MAX","RU",null,null,"Str",null);
        when(cs.updateCinema(1L,data)).thenReturn(r);
        when(ss.getByCid(2L)).thenReturn(new SeanceInfo[]{new SeanceInfo(2L,11L,date)});

        mockMvc.perform(patch("/v1/api/cinemas/1").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(data)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.cinema.id").value(2L))
                .andExpect(jsonPath("$.cinema.name").value("MAX"))
                .andExpect(jsonPath("$.cinema.country").value("RUS"))
                .andExpect(jsonPath("$.cinema.city").value("CITY"))
                .andExpect(jsonPath("$.cinema.region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.cinema.street").value("Street"))
                .andExpect(jsonPath("$.seances").exists())
                .andExpect(jsonPath("$.seances").isArray())
                .andExpect(jsonPath("$.seances[0].cid").value(2L))
                .andExpect(jsonPath("$.seances[0].fid").value(11L))
                .andExpect(jsonPath("$.seances[0].date").value(date.toString()));

    }

    @Test
    void testAllSeances() throws Exception {
        logger.info("testAllSeances");
        SeanceInfo[] e = new SeanceInfo[1];
        e[0] = new SeanceInfo(11L,12L,LocalDate.now());
        when(ss.getAll()).thenReturn(e);
        mockMvc.perform(get("/v1/api/seances").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cid").value(11L))
                .andExpect(jsonPath("$[0].fid").value(12L))
                .andExpect(jsonPath("$[0].date").value(e[0].getDate().toString()));
    }
}