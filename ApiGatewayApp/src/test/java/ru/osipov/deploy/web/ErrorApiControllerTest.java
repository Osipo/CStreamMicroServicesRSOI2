package ru.osipov.deploy.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.osipov.deploy.errors.ApiException;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.models.FilmInfo;
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

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiController.class)
@AutoConfigureMockMvc
public class ErrorApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    private static final Logger logger = LoggerFactory.getLogger(ErrorApiControllerTest.class);

    @MockBean
    WebFilmService fs;

    @MockBean
    WebGenreService gs;

    @MockBean
    WebCinemaService cs;

    @MockBean
    WebSeanceService ss;

    @Test
    void testIdFilmNotFound() throws Exception {
        logger.info("testIdFilmNotFound");
        doThrow(new ApiException("not found.",new IllegalStateException("original."),404,null,"NOT FOUND 404",null,null))
                .when(fs).getById(1L);

        mockMvc.perform(get("/v1/api/films/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.reason").value("NOT FOUND 404"))
                .andExpect(jsonPath("$.ex").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void testIdGenreNotFound() throws Exception {
        logger.info("testIdGenreNotFound");
        doThrow(new ApiException("not found.",new IllegalStateException("original."),404,null,"NOT FOUND!!!",null,null))
                .when(gs).getById(111L);
        mockMvc.perform(get("/v1/api/genres/111").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.reason").value("NOT FOUND!!!"))
                .andExpect(jsonPath("$.ex").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void testIdCinemaNotFound() throws Exception {
        logger.info("testIdCinemaNotFound");
        doThrow(new ApiException("not found.",new IllegalStateException("origin"),404,null,"NOT FOUND!!!",null,null))
                .when(cs).getById(anyLong());
        mockMvc.perform(get("/v1/api/cinemas/23").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.ex").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.reason").value( "NOT FOUND!!!"));
    }

    @Test
    void testSeancePKNotFound() throws Exception {
        logger.info("testSeancePKNotFound");
        doThrow(new ApiException("not found.",new IllegalStateException("origin"),404,null,"NOT FOUND!!!",null,null))
                .when(ss).getByCidFid(longThat(x -> x < 0),longThat(x -> x < 0));
        when(cs.getById(longThat(x -> x < 0))).thenReturn(new CinemaInfo(-1L,"D","C","C","R","Streeet"));
        mockMvc.perform(get("/v1/api/cinemas/-2/seances/-6").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.reason").value("NOT FOUND!!!"))
                .andExpect(jsonPath("$.code").value(404));
    }
}
