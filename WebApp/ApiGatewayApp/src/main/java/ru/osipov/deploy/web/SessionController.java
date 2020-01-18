package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.oauth.OAuthClient;
import ru.osipov.deploy.models.oauth.RegistrationOAuthClientDto;
import ru.osipov.deploy.models.oauth.TokenObject;
import ru.osipov.deploy.models.sign.SignUpRequest;
import ru.osipov.deploy.models.user.UserModel;
import ru.osipov.deploy.services.SessionService;
import ru.osipov.deploy.services.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/api")
@Validated
public class SessionController {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private SessionService sessionService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/auth/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserModel findUserById(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        logger.info("GET http://{}/v1/api/oauth/users/{}: findUserById() method called.", headers.getHost(), id);
        return sessionService.findUserById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/auth/clients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OAuthClient getClientById(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        logger.info("GET http://{}/v1/api/oauth/clients/{}: findClientById() method called.", headers.getHost(), id);
        return sessionService.findClientById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenObject createUser(@Valid @RequestBody SignUpRequest user, @RequestHeader HttpHeaders headers) {
        logger.info("POST http://{}/v1/api/users: createUser() method called.", headers.getHost());
        return sessionService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/auth/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity user, @RequestHeader HttpHeaders headers,
                           HttpServletRequest request) {
        logger.info("PUT http://{}/v1/api/oauth/users/{}: updateUser() method called.", headers.getHost(), id);
        sessionService.updateUser(id, user, jwtTokenProvider.resolveToken(request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/auth/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteUser(@PathVariable Long id, @RequestHeader HttpHeaders headers, HttpServletRequest request) {
        logger.info("DELETE http://{}/v1/api/oauth/users/{}: deleteUser() method called.", headers.getHost(), id);
        sessionService.deleteUser(id, jwtTokenProvider.resolveToken(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/auth/clients", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OAuthClient createClient(@Valid @RequestBody RegistrationOAuthClientDto clientDto, @RequestHeader HttpHeaders headers) {
        logger.info("POST http://{}/v1/api/clients: createClient() method called.", headers.getHost());
        return sessionService.createClient(clientDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/auth/clients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateClient(@PathVariable UUID id, @Valid @RequestBody OAuthClient client, @RequestHeader HttpHeaders headers,
                             HttpServletRequest request) {
        logger.info("PUT http://{}/v1/api/oauth/clients/{}: updateClient() method called.", headers.getHost(), id);
        sessionService.updateClient(id, client, jwtTokenProvider.resolveToken(request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/auth/clients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteClient(@PathVariable UUID id, @RequestHeader HttpHeaders headers, HttpServletRequest request) {
        logger.info("DELETE http://{}/v1/api/oauth/clients/{}: deleteClient() method called.", headers.getHost(), id);
        sessionService.deleteClient(id, jwtTokenProvider.resolveToken(request));
    }
}
