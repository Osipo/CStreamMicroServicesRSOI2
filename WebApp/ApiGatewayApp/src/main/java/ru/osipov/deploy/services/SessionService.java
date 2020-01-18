package ru.osipov.deploy.services;

import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.oauth.*;
import ru.osipov.deploy.models.sign.SignInRequest;
import ru.osipov.deploy.models.sign.SignUpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface SessionService {
    Map<String, String> getTokenByForm(HashMap<String, String> requestDto, String token);
    Map<String, String> getTokenByJson(HashMap<String, String> requestDto, String token);
    Map<String, String> getTokenByLForm(SignInRequest data, String token);
    Boolean validityToken(HashMap<String, String> requestDto);
    UserEntity findUserById(Long id);
    OAuthClient findClientById(UUID id);
    TokenObject createUser(SignUpRequest user);
    OAuthClient createClient(RegistrationOAuthClientDto clientDto);
    void updateUser(Long id, UserEntity user, String token);
    void updateClient(UUID id, OAuthClient client, String token);
    void deleteUser(Long id, String token);
    void deleteClient(UUID id, String token);
}
