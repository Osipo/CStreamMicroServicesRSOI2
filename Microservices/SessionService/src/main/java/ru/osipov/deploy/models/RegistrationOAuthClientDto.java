package ru.osipov.deploy.models;

import ru.osipov.deploy.entities.oauth.OAuthClient;

import java.io.Serializable;
import java.util.UUID;

public class RegistrationOAuthClientDto implements Serializable {

    private String scope;
    private String redirectUri;
    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public OAuthClient from() {
        OAuthClient client = new OAuthClient();
        client.setScope(scope);
        client.setApplicationName(applicationName);
        client.setClientSecret(UUID.randomUUID().toString());
        client.setRedirectUri(redirectUri);
        return client;
    }
}
