package ru.osipov.deploy.services.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osipov.deploy.exceptions.HttpNotFoundException;
import ru.osipov.deploy.repositories.AuthorizationCodeRepository;
import ru.osipov.deploy.entities.oauth.AuthorizationCode;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class AuthorizationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationCodeService.class);

    @Autowired
    private AuthorizationCodeRepository repository;

    public AuthorizationCode findById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new HttpNotFoundException("Not found authorization code")
        );
    }

    public AuthorizationCode generationCode(Long userId, UUID clientId, String redirectUri) {
        AuthorizationCode code = new AuthorizationCode();
        code.setValidity(true);
        code.setUserId(userId);
        code.setClientId(clientId);
        code.setRedirectUri(redirectUri);
        code.setCode(UUID.randomUUID());
        return repository.saveAndFlush(code);
    }

    public boolean validateCode(UUID code) {
        AuthorizationCode authorizationCode = findById(code);

        if (!authorizationCode.getCode().equals(code))
            return false;

        if (authorizationCode.getUpdatedAt() != null) {
            LocalDate date = authorizationCode.getUpdatedAt();
            if (date.isBefore(LocalDate.now())) {
                deleteById(code);
                return false;
            }
        } else if (authorizationCode.getCreatedAt() != null) {
            LocalDate date = authorizationCode.getCreatedAt();
            if (date.isBefore(LocalDate.now())) {
                deleteById(code);
                return false;
            }
        }
        return true;
    }

    public AuthorizationCode create(AuthorizationCode code) {
        return repository.saveAndFlush(code);
    }

    public AuthorizationCode update(AuthorizationCode code) {
        return repository.saveAndFlush(code);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
