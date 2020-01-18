package ru.osipov.deploy.entities.oauth;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.osipov.deploy.entities.DateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "oauth_client")
@EntityListeners(value = AuditingEntityListener.class)
public class OAuthClient implements Serializable {
    private static final long serialVersionUID = 43127469381264723L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "client_id", nullable = false, updatable = false, unique = true)
    private UUID clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "redirect_uri", nullable = false)
    private String redirectUri;

    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @Column(name = "grant_password")
    private boolean grantPassword;

    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private LocalDate updatedAt;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isGrantPassword() {
        return grantPassword;
    }

    public void setGrantPassword(boolean grantPassword) {
        this.grantPassword = grantPassword;
    }
}
