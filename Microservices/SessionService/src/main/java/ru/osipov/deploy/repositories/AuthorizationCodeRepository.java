package ru.osipov.deploy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.oauth.AuthorizationCode;

import java.util.UUID;

@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, UUID> {
    AuthorizationCode findByUserId(Long id);
}
