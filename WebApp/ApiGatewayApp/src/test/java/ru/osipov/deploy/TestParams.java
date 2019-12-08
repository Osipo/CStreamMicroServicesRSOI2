package ru.osipov.deploy;

import ru.osipov.deploy.models.FilmInfo;
import ru.osipov.deploy.models.GenreInfo;

public interface TestParams {
    FilmInfo[] FILMS = {
            new FilmInfo(1L,"IT_1",(short)16,21L),
            new FilmInfo(2L,"IT_2",(short)18,23L),
            new FilmInfo(3L,"IT_3",(short)20,21L),
            new FilmInfo(4L,"IT_4",(short)15,23L),
            new FilmInfo(5L,"IT_5",(short)16,23L)
    };
    GenreInfo[] GENRES = {
            new GenreInfo(1L,"Action_1","Some."),
            new GenreInfo(2L,"Action_12","Somet."),
            new GenreInfo(3L,"Action_123","Someth."),
            new GenreInfo(4L,"Action_1234","Somethi."),
            new GenreInfo(5L,"Action_122345","Somethin."),
            new GenreInfo(6L,"Action_166","Something."),
            new GenreInfo(7L,"Action_1777","Something is."),
            new GenreInfo(8L,"Action_18888","Something is gone."),
            new GenreInfo(9L,"Action_199999","Some134134.")
    };
}
