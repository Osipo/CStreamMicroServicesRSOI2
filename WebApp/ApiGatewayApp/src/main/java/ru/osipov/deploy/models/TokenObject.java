package ru.osipov.deploy.models;

import java.util.HashMap;
import java.util.Map;

public class TokenObject {
    private String accessToken;
    private Long expiresIn;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("expires_in", expiresIn.toString());
        map.put("token_type", tokenType);
        return map;
    }
}
