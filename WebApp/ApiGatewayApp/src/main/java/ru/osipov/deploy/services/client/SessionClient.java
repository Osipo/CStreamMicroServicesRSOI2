package ru.osipov.deploy.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.FeignConfig;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.oauth.OAuthClient;
import ru.osipov.deploy.models.oauth.RegistrationOAuthClientDto;
import ru.osipov.deploy.models.oauth.TokenObject;
import ru.osipov.deploy.models.sign.SignInRequest;
import ru.osipov.deploy.models.sign.SignUpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "session-service", configuration = FeignConfig.class, fallbackFactory = SessionFallbackFactory.class)
public interface SessionClient {

    @PostMapping(value = "/v1/api/oauth/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, String> getTokenByForm(@RequestParam HashMap<String, String> requestDto,
                                       @RequestHeader("Authorization") String token);

    @PostMapping(path = "/v1/api/oauth2/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getTokenByLForm(@RequestBody @Valid SignInRequest data, @RequestHeader("Authorization") String token);

    @PostMapping(value = "/v1/api/oauth/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, String> getTokenByJson(@RequestBody HashMap<String, String> requestDto,
                                       @RequestHeader("Authorization") String token);

    @PostMapping(value = "/v1/api/oauth/token/validity", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Boolean validityToken(@RequestBody HashMap<String, String> requestDto);


    @GetMapping(value = "/v1/api/users/{id}")
    Optional<UserEntity> findUserById(@PathVariable Long id);

    @GetMapping(value = "/v1/api/clients/{id}")
    Optional<OAuthClient> findClientById(@PathVariable UUID id);

    @PostMapping(value = "/v1/api/users/reg")
    Optional<TokenObject> createUser(@RequestBody SignUpRequest user);

    @PostMapping(value = "/v1/api/clients/reg")
    Optional<OAuthClient> createClient(@RequestBody RegistrationOAuthClientDto client);

    @PutMapping(value = "/v1/api/users/{id}")
    void updateUser(@PathVariable Long id, @RequestBody UserEntity user, @RequestHeader("Authorization") String token);

    @PutMapping(value = "/v1/api/clients/{id}")
    void updateClient(@PathVariable UUID id, @RequestBody OAuthClient client, @RequestHeader("Authorization") String token);

    @DeleteMapping(value = "/v1/api/users/{id}")
    void deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @DeleteMapping(value = "/v1/api/clients/{id}")
    void deleteClient(@PathVariable UUID id, @RequestHeader("Authorization") String token);

}
