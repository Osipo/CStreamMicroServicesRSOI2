package ru.osipov.deploy.services.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.repositories.OAuthTokenRepository;
import ru.osipov.deploy.entities.oauth.OAuthToken;

import java.util.UUID;

@Service
public class OAuthTokenService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthTokenService.class);

    @Autowired
    private OAuthTokenRepository repository;

    public OAuthToken findByClientId(UUID id) {
        return repository.findByClientId(id);
    }

    public OAuthToken findByUserId(Long id) {
        return repository.findByUserId(id);
    }

    public OAuthToken create(OAuthToken token) {
        return repository.saveAndFlush(token);
    }

    public OAuthToken update(OAuthToken token) {
        return repository.saveAndFlush(token);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public void deleteByClientId(UUID id) {
        repository.deleteByClientId(id);
    }

    public void deleteByUserId(Long id){repository.deleteByUserId(id);}
}
