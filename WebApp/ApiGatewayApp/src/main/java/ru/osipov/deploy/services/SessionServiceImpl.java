package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.errors.HttpCanNotCreateException;
import ru.osipov.deploy.errors.JwtAuthenticationException;
import ru.osipov.deploy.errors.ServiceAccessException;
import ru.osipov.deploy.models.oauth.OAuthClient;
import ru.osipov.deploy.models.oauth.RegistrationOAuthClientDto;
import ru.osipov.deploy.models.oauth.TokenObject;
import ru.osipov.deploy.models.sign.SignInRequest;
import ru.osipov.deploy.models.sign.SignUpRequest;
import ru.osipov.deploy.models.user.UserModel;
import ru.osipov.deploy.services.client.SessionClient;
import ru.osipov.deploy.services.jwt.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    private SessionClient sessionClient;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Map<String, String> getTokenByForm(HashMap<String, String> requestDto, String t) {
        logger.info("getTokenByForm() method called:");
        Map<String, String> token = sessionClient.getTokenByForm(requestDto, "Bearer " + t);
        if (token != null && token.get("access_token") == null)
            throw new ServiceAccessException("Session service unavailable.");
        logger.info("\t" + token);
        return token;
    }

    @Override
    public Map<String, String> getTokenByJson(HashMap<String, String> requestDto, String t) {
        logger.info("getTokenByJson() method called:");
        Map<String, String> token = sessionClient.getTokenByJson(requestDto, "Bearer " + t);
        if (token != null && token.get("access_token") == null)
            throw new ServiceAccessException("Session service unavailable.");
        logger.info("\t" + token);
        return token;
    }

    @Override
    public Map<String, String> getTokenByLForm(SignInRequest data, String t) {
        logger.info("getTokenByLForm() method called:");
        Map<String, String> token = sessionClient.getTokenByLForm(data,"Bearer "+t);
        if (token != null && token.get("access_token") != null)
            throw new ServiceAccessException("Session service unavailable.");
        logger.info("\t" + token);
        return token;
    }

    @Override
    public Boolean validityToken(HashMap<String, String> requestDto) {
        logger.info("validityToken() method called:");
        Boolean isValidate = sessionClient.validityToken(requestDto);
        if (isValidate == null)
            throw new ServiceAccessException("Session service unavailable.");
        logger.info("\t" + isValidate);
        return isValidate;
    }


    @Override
    public UserModel findUserById(Long id) {
        logger.info("findUserById() method called:");
        UserModel user = sessionClient.findUserById(id)
                .orElseThrow(() -> new HttpCanNotCreateException("User could not be found"));

        if (user.getId().equals(-1L))
            throw new ServiceAccessException("Session service unavailable.");

        logger.info("\t" + user);
        return user;
    }


    @Override
    public OAuthClient findClientById(UUID id) {
        logger.info("findClientById() method called:");
        OAuthClient client = sessionClient.findClientById(id)
                .orElseThrow(() -> new HttpCanNotCreateException("OAuthClient could not be found"));

        UUID zeroUUID = new UUID(0, 0);
        if (client.getClientId().equals(zeroUUID))
            throw new ServiceAccessException("Session service unavailable.");

        logger.info("\t" + client);
        return client;
    }

    @Override
    public TokenObject createUser(SignUpRequest user) {
        logger.info("createUser() method called:");
        TokenObject result = sessionClient.createUser(user)
                .orElseThrow(() -> new HttpCanNotCreateException("User could not be created"));

        UUID zeroUUID = new UUID(0, 0);
        if (result == null)
            throw new ServiceAccessException("Session service unavailable.");
        logger.info("\t" + result);
        return result;
    }

    @Override
    public void updateUser(Long id, UserEntity user, String token) {
        logger.info("updateUser() method called.");
        checkToken(token);
        UserModel checkUser = sessionClient.findUserById(id)
                .orElseThrow(() -> new HttpCanNotCreateException("User could not be checked"));

        if (checkUser.getId().equals(-1L))
            throw new ServiceAccessException("Session service unavailable.");

        user.setId(id);

        sessionClient.updateUser(id, user, "Bearer " + token);
    }

    @Override
    public void deleteUser(Long id, String token) {
        logger.info("deleteUser() method called.");
        checkToken(token);
        UserModel user = sessionClient.findUserById(id)
                .orElseThrow(() -> new HttpCanNotCreateException("User could not be checked"));

        if (user.getId().equals(-1L))
            throw new ServiceAccessException("Session service unavailable.");

        sessionClient.deleteUser(id, "Bearer " + token);
    }

    @Override
    public OAuthClient createClient(RegistrationOAuthClientDto clientDto) {
        logger.info("createClient() method called:");
        OAuthClient result = sessionClient.createClient(clientDto)
                .orElseThrow(() -> new HttpCanNotCreateException("OAuthClient could not be created"));

        UUID zeroUUID = new UUID(0, 0);
        if (result.getClientId().equals(zeroUUID))
            throw new ServiceAccessException("Session service unavailable.");
        logger.info("\t" + result);
        return result;
    }

    @Override
    public void updateClient(UUID id, OAuthClient client, String token) {
        logger.info("updateClient() method called.");
        checkToken(token);
        OAuthClient checkClient = sessionClient.findClientById(id)
                .orElseThrow(() -> new HttpCanNotCreateException("OAuthClient could not be checked"));

        UUID zeroUUID = new UUID(0, 0);
        if (checkClient.getClientId().equals(zeroUUID))
            throw new ServiceAccessException("Session service unavailable.");

        client.setClientId(id);

        sessionClient.updateClient(id, client, "Bearer " + token);
    }

    @Override
    public void deleteClient(UUID id, String token) {
        logger.info("deleteClient() method called.");
        checkToken(token);
        OAuthClient client = sessionClient.findClientById(id)
                .orElseThrow(() -> new HttpCanNotCreateException("OAuthClient could not be checked"));

        UUID zeroUUID = new UUID(0, 0);
        if (client.getClientId().equals(zeroUUID))
            throw new ServiceAccessException("Session service unavailable.");

        sessionClient.deleteClient(id, "Bearer " + token);
    }

    private void checkToken(String token) {
        boolean t = jwtTokenProvider.validateAccessToken(token);

        if (!t)
            throw new JwtAuthenticationException("Token is invalid.");
    }
}