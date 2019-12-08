package ru.osipov.deploy.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.osipov.deploy.GatewayApp;
import ru.osipov.deploy.models.*;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GatewayApp.class})
public class ModelsTest {

    Logger logger = LoggerFactory.getLogger(ModelsTest.class);
    @Test
    void testEquals(){
        CreateSeance s1 = new CreateSeance();
        CreateSeance s2 = new CreateSeance();
        s1.setFid(2L);
        s1.setCid(3L);
        s2.setFid(3L);
        s2.setCid(2L);
        LocalDate date = LocalDate.now();
        s1.setDate(date);s2.setDate(date);
        assert !s1.equals(s2);

        CreateSeance s3 = new CreateSeance();
        s3.setFid(2L);s3.setCid(3L);s3.setDate(date);
        logger.info("Message '{}' ",s3.toString());
        assert s1.equals(s3);
        assert s1.toString().equals(s3.toString());
        assert s1.toString() != s3.toString();
    }

    @Test
    void testCreateCinema(){
        CreateCinema c1 = new CreateCinema("N12","C12","CC2","","Str",null);
        CreateCinema c3 = new CreateCinema("N12","C12","CC2","","Str",new CreateSeance[]{});
        CreateCinema c4 = new CreateCinema("N12","C12","CC2",null,"Str",null);
        CreateCinema c2 = new CreateCinema();
        c2.setName("N12");
        c2.setCountry("C12");
        c2.setCity("CC2");
        c2.setRegion("");
        c2.setStreet("Str");
        assert c2.getName().equals("N12");
        assert c2.getCountry().equals("C12");
        assert c2.getCity().equals("CC2");
        assert c2.getRegion().equals("");
        assert c2.getStreet().equals("Str");
        assert c2.getSeances() == null;


        assert c1.equals(c2);
        assert !c1.equals(c3);
        assert !c1.equals(c4);
        assert c1.hashCode() == c2.hashCode();
        assert c3.hashCode() != c4.hashCode();
    }

    @Test
    void testCinemaInfo(){
        CinemaInfo i1 = new CinemaInfo(1L,"IT","RUS","C1","","str");
        CinemaInfo i2 = new CinemaInfo(1L,"IT","RUS","C1","","str");
        assert i2.getName().equals("IT");
        assert i2.getId() == 1L;
        assert i2.getCountry().equals("RUS");
        assert i2.getCity().equals("C1");
        assert i2.getRegion().equals("");
        assert i2.getStreet().equals("str");
        CinemaInfo i3 = new CinemaInfo(1L,"IT","RUS","C1",null,"str");
        assert !i1.equals(i3);
        assert i1.equals(i2);
        assert i1.toString().equals(i2.toString());
        assert i1.hashCode() == i2.hashCode();
        assert i2.getRegion().equals("");
        assert i3.getRegion() == null;
        assert i1.hashCode() == i3.hashCode();//by region.
    }

    @Test
    void testGenreInfo(){
        GenreInfo g1 = new GenreInfo(1L,"Name","");
        GenreInfo g2 = new GenreInfo(1L,"Name","");
        GenreInfo g3 = new GenreInfo(2L,"Name",null);
        GenreInfo g4 = new GenreInfo(2L,"name",null);
        assert g1.getId() == 1L;
        assert g1.getName().equals("Name");
        assert g1.getRemarks().equals("");
        assert g3.getRemarks() == null;
        assert g1.equals(g2);
        assert !g1.equals(g3);
        assert g1.toString().equals(g2.toString());
        logger.info("s3: '{}'\n s4: '{}'",g3.toString().toLowerCase(), g4.toString().toLowerCase());
        assert g3.toString().toLowerCase().equals(g4.toString().toLowerCase());
        assert !g3.toString().equals(g4.toString());
        assert g1.hashCode() == g2.hashCode();
    }

    @Test
    void testSeanceInfo(){
        SeanceInfo sc1 = new SeanceInfo(1L,2L,LocalDate.now());
        LocalDate date2 = LocalDate.now();
        SeanceInfo sc2 = new SeanceInfo(2L,2L,date2);
        SeanceInfo sc3 = new SeanceInfo(2L,2L,date2);
        SeanceInfo sc4 = new SeanceInfo();
        assert sc2.getCid() == 2L;
        assert sc2.getFid() == 2L;
        assert sc2.getDate().toString().equals(date2.toString());

        assert sc4.getCid() == -1L;
        assert sc4.getFid() == -1L;
        assert sc4.getDate().toString().equals(date2.toString());

        assert !sc1.equals(sc2);
        assert sc1.hashCode() != sc2.hashCode();
        assert sc2.equals(sc3);
        assert sc2.hashCode() == sc3.hashCode();
        assert !sc1.toString().equals(sc2.toString());
        assert sc2.toString().equals(sc3.toString());
    }

    @Test
    void testSetCinemaSeances(){
        CinemaSeances cs = new CinemaSeances();
        CinemaInfo c = new CinemaInfo(1L,"N","RU","C","","Str");
        cs.setCinema(c);
        cs.setSeances(new SeanceInfo[]{});
        CinemaSeances cs2 = new CinemaSeances(c,null);
        CinemaSeances cs3 = new CinemaSeances(c,null);
        assert !cs.equals(cs2);
        assert cs.getCinema().equals(cs2.getCinema());
        assert cs.hashCode() != cs2.hashCode();
        assert cs2.hashCode() == cs3.hashCode();
        assert cs2.equals(cs3);
    }

    @Test
    void testNewGenre(){
        NewGenre g1 = new NewGenre();
        g1.setVal(1L);
        NewGenre g2 = new NewGenre(1L);
        NewGenre g3 = new NewGenre(4L);
        NewGenre g4 = new NewGenre(null);
        assert g4.getVal() == null;
        assert g1.getVal() == 1L;
        assert g1.equals(g2);
        assert g1.hashCode() == g2.hashCode();
        assert g1.toString().equals(g2.toString());
        assert g2.hashCode() != g3.hashCode();
        assert !g2.equals(g3);
        assert !g2.toString().equals(g3.toString());
        assert g4.toString() != null;
    }
}
