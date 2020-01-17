package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.oauth.OAuthToken;
import java.util.UUID;

@Repository
public interface OAuthTokenRepository extends JpaRepository<OAuthToken, UUID> {
    OAuthToken findByClientId(UUID clientId);
    OAuthToken findByRefreshToken(String refreshToken);
    OAuthToken findByUserId(Long userId);
    void deleteByClientId(UUID id);
    void deleteByUserId(Long id);
}
