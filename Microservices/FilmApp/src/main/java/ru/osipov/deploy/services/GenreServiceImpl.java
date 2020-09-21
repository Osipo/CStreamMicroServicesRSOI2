package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Genre;
import ru.osipov.deploy.models.CreateGenreR;
import ru.osipov.deploy.models.GenreInfo;
import ru.osipov.deploy.repositories.GenreRepository;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository gRepository;
    private static final Logger logger = getLogger(GenreServiceImpl.class);

    @Autowired
    public GenreServiceImpl(GenreRepository gr){
        this.gRepository = gr;
    }


    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public GenreInfo getGenreById(Long id) {
        logger.info("Get genre by id = '{}'",id);
        Optional<Genre> o = gRepository.findByGid(id);
        if(o.isPresent()){
            logger.info("Genre was found. Return object.");
            return buildModel(o.get());
        }
        else{
            logger.info("Genre was not found. Throw exception...");
            throw new IllegalStateException("Genre with "+id+" was not found.");
        }
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<GenreInfo> getAllGenres() {
        logger.info("Get all genres");
        return gRepository.findAll()
                .stream()
                .map(this::buildModel)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public GenreInfo getByName(@Nonnull String name) {
        logger.info("Get genre by name = '{}'",name);
        Optional<GenreInfo> o =  gRepository.findByName(name).map(this::buildModel);
        return o.orElseGet(() -> new GenreInfo(-2l, "", ""));
    }

    @Override
    @Transactional
    public GenreInfo updateGenre(@Nonnull String oldname, @Nonnull String nname) throws IllegalStateException{
        Optional<Genre> o = gRepository.findByName(oldname);
        Optional<Genre> o2 = gRepository.findByName(nname);
        Genre g;
        if(o.isPresent() && o2.isEmpty()){
            logger.info("Update genre with name = '{}'",oldname);
            g = o.get();
            g.setName(nname);
            logger.info("New name = '{}'",g.getName());
            gRepository.save(g);
            return buildModel(g);
        }
        else if(o2.isPresent()){
            logger.info("The newName of the genre = '{}' must be unique",oldname);
            logger.info("But this name '{}' is already being used by another genre",nname);
            throw new IllegalStateException("Genre with new name"+nname+"alredy exists");
        }
        else{
            logger.info("not found genre with name = '{}'",oldname);
            throw new IllegalStateException("Genre with old name"+oldname+"does not exist.");
        }
    }


    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<GenreInfo> getByRemarks(String remarks) {
        logger.info("Get genres by remarks = '{}'",remarks);
         return gRepository.findByRemarks(remarks)
                .stream()
                .map(this::buildModel).collect(Collectors.toList());
    }

    @Override
    @Nonnull
    @Transactional
    public URI createGenre(@Nonnull CreateGenreR request) {
        logger.info("Creating genre...");
        logger.info("Vals:\n\t name = '{}'\n\t remarks = '{}'",request.getGname(),request.getRemarks());
        Genre g = new Genre()
                .setName(request.getGname())
                .setRemarks(request.getRemarks());
        g = gRepository.save(g);
        //GenreInfo ng = getById(g.getGid());
        logger.info("Successful created.");
        return URI.create("/v1/genres/"+g.getGid());
    }

    public URI restoreGenre(@Nonnull GenreInfo request){
        logger.info("Restoring genre...");
        logger.info("Vals:\n\t name = '{}'\n\t remarks = '{}'",request.getName(),request.getRemarks());
        Genre g = new Genre()
                .setGid(request.getId())//RESTORE ID.
                .setName(request.getName())
                .setRemarks(request.getRemarks());
        g = gRepository.save(g);
        logger.info("Successful created.");
        return URI.create("/v1/genres/"+g.getGid());
    }

    @Override
    @Transactional
    public GenreInfo updateGenre(Long id,@Nonnull CreateGenreR request) {
        logger.info("Updating genre...");
        Optional<Genre> o = gRepository.findByGid(id);
        if(o.isPresent()){
            logger.info("Genre was found. Retrieve object for operation");
            Genre g = o.get();
            Optional<Genre> o2 = gRepository.findByName(request.getGname());
            if(o2.isPresent()){
                logger.info("The newName of the new genre  must be unique");
                logger.info("But this name '{}' is already being used by another genre",request.getGname());
                throw new IllegalStateException("Genre with new name "+request.getGname()+" alredy exists");
            }
            g.setName(request.getGname());
            g.setRemarks(request.getRemarks());
            logger.info("Data was read.");
            gRepository.save(g);
            logger.info("Genre was changed.");
            return buildModel(g);
        }
        else{
            logger.info("Genre with id '{}' was not found. Throw exception IllegalState",id);
            throw new IllegalStateException("Genre with id "+id+"was not found.");
        }
    }

    @Override
    @Transactional
    public GenreInfo deleteGenre(@Nonnull Long id) throws IllegalStateException{
        logger.info("Delete genre by id =  '{}",id);
        Optional<Genre> o = gRepository.findByGid(id);
        if(o.isPresent()){
            logger.info("Genre was found.");
            GenreInfo r = buildModel(o.get());
            gRepository.delete(o.get());
            logger.info("Deleted successful.");
            return r;
        }
        else{
            logger.info("Genre was not found.");
            throw new IllegalStateException("Genre with id "+id+" was not found.");
        }
    }

    @Nonnull
    private GenreInfo buildModel(@Nonnull Genre gi) {
        logger.info("Genre: '{}'",gi);
        return new GenreInfo(gi.getGid(),gi.getName(), gi.getRemarks());
    }
}
