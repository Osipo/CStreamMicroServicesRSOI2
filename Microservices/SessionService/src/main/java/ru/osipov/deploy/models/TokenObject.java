package ru.osipov.deploy.models;

import ru.osipov.deploy.entities.oauth.OAuthToken;

import java.util.HashMap;
import java.util.Map;

public class TokenObject {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public static TokenObject from(OAuthToken token) {
        TokenObject tokenObject = new TokenObject();
        tokenObject.setRefreshToken(token.getRefreshToken());
        tokenObject.setAccessToken(token.getAccessToken());
        tokenObject.setTokenType("bearer");
        return tokenObject;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        map.put("expires_in", expiresIn.toString());
        map.put("token_type", tokenType);
        return map;
    }
}
