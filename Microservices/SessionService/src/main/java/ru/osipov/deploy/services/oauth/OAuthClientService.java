package ru.osipov.deploy.services.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.repositories.OAuthClientRepository;
import ru.osipov.deploy.entities.oauth.OAuthClient;

import java.util.UUID;

@Service
public class OAuthClientService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthClientService.class);

    @Autowired
    private OAuthClientRepository repository;

    public OAuthClient findById(UUID clientId) {
        return repository.findByClientId(clientId);
    }

    public Page<OAuthClient> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public OAuthClient create(OAuthClient client) {
        return repository.saveAndFlush(client);
    }

    public OAuthClient update(UUID id, OAuthClient client) {
        client.setClientId(id);
        return repository.saveAndFlush(client);
    }

    public void deleteById(UUID clientId) {
        repository.deleteById(clientId);
    }
}
