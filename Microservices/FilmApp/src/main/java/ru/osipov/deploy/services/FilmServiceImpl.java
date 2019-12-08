package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Film;
import ru.osipov.deploy.models.CreateFilm;
import ru.osipov.deploy.models.FilmInfo;
import ru.osipov.deploy.repositories.FilmRepository;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.system.SystemProperties.get;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository rep;

    private static final Logger logger = getLogger(FilmServiceImpl.class);

    @Autowired
    public FilmServiceImpl(FilmRepository r){
        this.rep = r;
    }

    @Nonnull
    @Transactional(readOnly = true)
    @Override
    public List<FilmInfo> getAllFilms() {
        return rep.findAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Transactional(readOnly = true)
    @Override
    public FilmInfo getByName(@Nonnull String name) {
        logger.info("Get film by name = '{}'",name);
        Optional<FilmInfo> o =  rep.findByFname(name).map(this::buildModel);
        return o.orElseGet(() -> new FilmInfo(-2l, "", (short)-1,-1l));
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public FilmInfo getFilmById(Long id) {
        logger.info("Get film by id = '{}'",id);
        Optional<Film> o = rep.findByFid(id);
        if(o.isPresent()){
            logger.info("Film was found. Return object.");
            return buildModel(o.get());
        }
        else{
            logger.info("Film was not found. Throw exception...");
            throw new IllegalStateException("Film was not found.");
        }
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<FilmInfo> getFilmsByGid(Long gid) {
        logger.info("Get films by genre_id = '{}'",gid);
        return rep.findByGid(gid).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Transactional(readOnly = true)
    @Override
    public List<FilmInfo> getByRating(@Nonnull Short rating) {
        logger.info("Get films by rating = '{}'",rating);
        return rep.findByRating(rating).stream().map(this::buildModel).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public FilmInfo updateFilmRating(String name, Short rating) throws IllegalStateException{
        Optional<Film> o = rep.findByFname(name);
        if(o.isPresent() && rating >= 0 && rating <= 100){
            logger.info("Update rating of the film = '{}'",name);
            Film f = o.get();
            f.setRating(rating);
            rep.save(f);
            logger.info("Updated successful.  New rating = '{}'",f.getRating());
            return buildModel(f);
        }
        else if(rating > 100 || rating < 0){
            logger.info("Illegal rating value '{}'",rating);
            throw new IllegalStateException("Illegal rating value. Must be[0..100]");
        }
        else{
            logger.info("not found film with name = '{}'",name);
            throw new IllegalStateException("Film with name"+name+"does not exist");
        }
    }

    @Nonnull
    @Override
    @Transactional
    public List<FilmInfo> updateGenre(Long oldgid, Long ngid) {
        logger.info("Change genre in all films with specified = '{}'",oldgid);

        List<Film> l = rep.findByGid(oldgid);
        List<FilmInfo> res = new ArrayList<>();
        logger.info("Films: "+l.size());
        if(l.size() > 0){
            logger.info("Updating...");
            for(int i = 0; i < l.size(); i++){
                Film f = l.get(i);
                f.setGid(ngid);
                rep.save(f);
                res.add(buildModel(f));
            }
        }
        return res;
    }

    @Override
    @Transactional
    public FilmInfo deleteFilm(String name) throws IllegalStateException{
        logger.info("Delete film by name = '{}'",name);
        Optional<Film> o = rep.findByFname(name);
        if(o.isPresent()){
            logger.info("Film was found.");
            FilmInfo r = buildModel(o.get());
            rep.delete(o.get());
            logger.info("Deleted successful.");
            return r;
        }
        else{
            logger.info("Film was not found.");
            throw new IllegalStateException("Film with name"+name+"was not found.");
        }
    }

    @Override
    @Transactional
    public FilmInfo updateFilm(Long id,@Nonnull CreateFilm request) {
        logger.info("updating film...");
        Optional<Film> o = rep.findByFid(id);
        if(o.isPresent()){
            Film f = o.get();
            logger.info("Film was found.");
            f.setFname(request.getName());
            f.setRating(request.getRating());
            f.setGid(request.getGid());
            logger.info("New values are set.");
            rep.save(f);
            logger.info("Successful saved.");
            return buildModel(f);
        }
        else{
            throw new IllegalStateException("Film with "+id+"not found.");
        }
    }


    @Nonnull
    private FilmInfo buildModel(@Nonnull Film fi) {
        logger.info("Cinema: '{}'",fi);
        return new FilmInfo(fi.getFid(),fi.getFname(), fi.getRating(),fi.getGid());
    }
}
