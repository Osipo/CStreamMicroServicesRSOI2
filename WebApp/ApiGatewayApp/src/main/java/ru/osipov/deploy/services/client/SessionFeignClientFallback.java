package ru.osipov.deploy.services.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.oauth.OAuthClient;
import ru.osipov.deploy.models.oauth.RegistrationOAuthClientDto;
import ru.osipov.deploy.models.oauth.TokenObject;
import ru.osipov.deploy.models.sign.SignInRequest;
import ru.osipov.deploy.models.sign.SignUpRequest;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class SessionFeignClientFallback implements SessionClient {
    private static final Logger logger = LoggerFactory.getLogger(SessionFeignClientFallback.class);
    private final Throwable cause;

    public SessionFeignClientFallback(Throwable cause) {
        this.cause = cause;
    }


    @Override
    public Map<String, String> getTokenByForm(HashMap<String, String> requestDto, String token) {
        logger.error("getTokenByForm() method called.");
        logger.error(cause.getMessage());
        Map<String, String> fallback = new HashMap<>();
        fallback.put("access_token", null);
        return fallback;
    }

    @Override
    public Map<String, String> getTokenByLForm(@Valid SignInRequest data, String token) {
        logger.error("getTokenByLForm() method called.");
        logger.error(cause.getMessage());
        Map<String, String> fallback = new HashMap<>();
        fallback.put("access_token", null);
        return fallback;
    }

    @Override
    public Map<String, String> getTokenByJson(HashMap<String, String> requestDto, String token) {
        logger.error("getTokenByJson() method called.");
        logger.error(cause.getMessage());
        Map<String, String> fallback = new HashMap<>();
        fallback.put("access_token", null);
        return fallback;
    }

    @Override
    public Boolean validityToken(HashMap<String, String> requestDto) {
        logger.error("getTokenByJson() method called.");
        logger.error(cause.getMessage());
        return null;
    }


    @Override
    public Optional<UserEntity> findUserById(Long id) {
        logger.error("findUserById() method called.");
        logger.error(cause.getMessage());
        UserEntity fallback = new UserEntity();
        fallback.setId(-1L);
        return Optional.of(fallback);
    }

    @Override
    public Optional<OAuthClient> findClientById(UUID id) {
        logger.error("findClientById() method called.");
        logger.error(cause.getMessage());
        OAuthClient fallback = new OAuthClient();
        fallback.setClientId(new UUID(0, 0));
        return Optional.of(fallback);
    }

    @Override
    public Optional<TokenObject> createUser(SignUpRequest data) {
        logger.error("createUser() method called.");
        logger.error(cause.getMessage());
        TokenObject tok = new TokenObject();
        tok.setAccessToken(null);
        return Optional.of(tok);
    }

    @Override
    public void updateUser(Long id, UserEntity user, String token) {
        logger.error("updateUser() method called.");
        logger.error(cause.getMessage());
    }

    @Override
    public void deleteUser(Long id, String token) {
        logger.error("deleteUser() method called.");
        logger.error(cause.getMessage());
    }

    @Override
    public Optional<OAuthClient> createClient(RegistrationOAuthClientDto client) {
        logger.error("createClient() method called.");
        logger.error(cause.getMessage());
        OAuthClient fallback = new OAuthClient();
        fallback.setClientId(new UUID(0, 0));
        return Optional.of(fallback);
    }

    @Override
    public void updateClient(UUID id, OAuthClient client, String token) {
        logger.error("updateClient() method called.");
        logger.error(cause.getMessage());
    }

    @Override
    public void deleteClient(UUID id, String token) {
        logger.error("deleteClient() method called.");
        logger.error(cause.getMessage());
    }
}
