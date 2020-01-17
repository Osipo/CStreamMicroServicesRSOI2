package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.*;
import ru.osipov.deploy.models.sign.SignUpRequest;
import ru.osipov.deploy.models.user.*;
import ru.osipov.deploy.services.oauth.*;
import ru.osipov.deploy.services.jwt.JwtTokenProvider;
import ru.osipov.deploy.entities.oauth.*;
import ru.osipov.deploy.services.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/api")
public class SessionController {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OAuthClientService oAuthClientService;
    private final AuthorizationCodeService authorizationCodeService;

    @Autowired
    public SessionController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                             UserService userService, OAuthClientService oAuthClientService, AuthorizationCodeService codeService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.oAuthClientService = oAuthClientService;
        this.authorizationCodeService = codeService;
    }

    @PostMapping(value = "/oauth/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map<String, String> getTokenByForm(@RequestParam HashMap<String, String> requestDto,
                                              HttpServletRequest request) {
        return getTokenByJson(requestDto, request);
    }

    @PostMapping(value = "/oauth/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> getTokenByJson(@RequestBody HashMap<String, String> requestDto,
                                              HttpServletRequest request) {
        switch (requestDto.get("grant_type")) {
            case "password":
                return getTokenByUsernameAndPassword(requestDto).toMap();
            case "refresh_token":
                return getTokenByRefreshToken(requestDto).toMap();
            case "authorization_code":
                return getTokenByCodeAuthorization(requestDto, request).toMap();
        }
        throw new BadCredentialsException("Undefined error");
    }

    @PostMapping(value = "/oauth/token/validity", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean validityToken(@RequestBody HashMap<String, String> requestDto) {
        String accessToken = requestDto.get("access_token");
        String refreshToken = requestDto.get("refresh_token");
        if (requestDto.size() > 1)
            throw new RuntimeException("Bad Request");
        if (accessToken != null) {
            return jwtTokenProvider.validateAccessToken(accessToken);
        }
        if (refreshToken != null) {
            return jwtTokenProvider.validateRefreshToken(refreshToken);
        }
        return false;
    }

    @GetMapping(value = "/oauth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AuthorizationCode getCode(@RequestParam(value = "response_type") String responseType,
                                     @RequestParam(value = "client_id") UUID clientId,
                                     @RequestParam(value = "redirect_uri") String redirectUri,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        if (responseType.equals("code")) {
            String token = jwtTokenProvider.resolveToken(request);
            if (token != null && jwtTokenProvider.validateAccessToken(token)) {
                Long userId = jwtTokenProvider.getUserIdByToken(token);
                return authorizationCodeService.generationCode(userId, clientId, redirectUri);
            }
        }
        throw new BadCredentialsException("Code Flow invalid.");
    }

//    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public Page<UserModel> findUsersAll(@RequestParam(value = "page") Integer page, @RequestParam(value = "size") Integer size) {
//        return userService.findAll(PageRequest.of(page, size));
//    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserModel findUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<OAuthClient> findClientsAll(@RequestParam(value = "page") Integer page, @RequestParam(value = "size") Integer size) {
        return oAuthClientService.findAll(PageRequest.of(page, size));
    }

    @GetMapping(value = "/clients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OAuthClient findClientById(@PathVariable UUID id) {
        return oAuthClientService.findById(id);
    }

    @PostMapping(value = "/users/reg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserDto createUser(@RequestBody @Valid SignUpRequest user) {
        UserEntity r = new UserEntity();
        r.setName(user.getName());
        r.setUsername(user.getUsername());
        r.setEmail(user.getEmail());
        r.setPassword(user.getPassword());
        UserEntity result = userService.create(r);//set role ROLE_USER and encrypt Password. Then save it into db.
        return new UserDto(result);
    }

    @PostMapping(value = "/clients/reg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OAuthClient createClient(@RequestBody RegistrationOAuthClientDto oAuthClientDto) {
        OAuthClient client = oAuthClientDto.from();
        return oAuthClientService.create(client);
    }

    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        userService.update(id, user);
    }

    @PutMapping(value = "/clients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateClient(@PathVariable UUID id, @RequestBody OAuthClient client) {
        oAuthClientService.update(id, client);
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    @DeleteMapping(value = "/clients/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteClient(@PathVariable UUID id) {
        oAuthClientService.deleteById(id);
    }

    private TokenObject getTokenByUsernameAndPassword(Map<String, String> requestDto) {
        if (requestDto.get("client_id") == null)
            throw new BadCredentialsException("Invalid - clientId: " + requestDto.get("client_id"));

        OAuthClient oAuthClient = oAuthClientService.findById(UUID.fromString(requestDto.get("client_id")));
        if (oAuthClient == null)
            throw new BadCredentialsException("Invalid - not found client by clientId: " + requestDto.get("client_id"));
        if (!oAuthClient.isGrantPassword())
            throw new BadCredentialsException("Invalid - Grant Password denied for client: " + requestDto.get("client_id"));

        String username = requestDto.get("username");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.get("password")));
        UserEntity user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        return jwtTokenProvider.createToken(user, oAuthClient);
    }

    private TokenObject getTokenByRefreshToken(Map<String, String> requestDto) {
        if (requestDto.get("client_id") == null)
            throw new BadCredentialsException("Invalid - clientId: " + requestDto.get("client_id"));

        OAuthClient oAuthClient = oAuthClientService.findById(UUID.fromString(requestDto.get("client_id")));
        if (oAuthClient == null)
            throw new BadCredentialsException("Invalid - not found client by clientId: " + requestDto.get("client_id"));
        if (!oAuthClient.getClientSecret().equals(requestDto.get("client_secret")))
            throw new BadCredentialsException("Invalid - client secret is not true");

        if (!jwtTokenProvider.validateRefreshToken(requestDto.get("refresh_token")))
            throw new BadCredentialsException("Invalid refresh token");

        return jwtTokenProvider.updateToken(requestDto.get("refresh_token"), oAuthClient.getClientId());
    }

    private TokenObject getTokenByCodeAuthorization(Map<String, String> requestDto, HttpServletRequest request) {
        String userToken = jwtTokenProvider.resolveToken(request);
        if (userToken != null && jwtTokenProvider.validateAccessToken(userToken)) {
            if (authorizationCodeService.validateCode(UUID.fromString(requestDto.get("code")))) {
                AuthorizationCode authorizationCode = authorizationCodeService
                        .findById(UUID.fromString(requestDto.get("code")));
                OAuthClient client = oAuthClientService
                        .findById(UUID.fromString(requestDto.get("client_id")));
                if (!client.getClientId().equals(authorizationCode.getClientId()))
                    throw new BadCredentialsException("ClientId is invalid.");
                if (!client.getClientSecret().equals(requestDto.get("client_secret")))
                    throw new BadCredentialsException("Client Secret is invalid");

                try {
                    UserModel userModel = userService.findById(authorizationCode.getUserId());
                    UserEntity user = new UserEntity();
                    user.setUsername(userModel.getUsername());
                    user.setRoles(userModel.getRoles());
                    user.setId(userModel.getId());
                    user.setEmail(userModel.getEmail());
                    authorizationCodeService.deleteById(authorizationCode.getCode());
                    return jwtTokenProvider.createToken(user, client);
                } catch (AuthenticationException e) {
                    throw new BadCredentialsException("Invalid code flow");
                }
            } else
                throw new BadCredentialsException("Authorization code is invalid");
        }
        throw new BadCredentialsException("User Token is invalid");
    }
}
