package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.oauth.OAuthClient;

import java.util.UUID;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, UUID> {
    OAuthClient findByClientId(UUID id);
}
