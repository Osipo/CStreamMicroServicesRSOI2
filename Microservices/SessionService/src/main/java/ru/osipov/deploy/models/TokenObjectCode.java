package ru.osipov.deploy.models;

import java.util.HashMap;
import java.util.Map;

public class TokenObjectCode extends TokenObject {
    private String redirectUri;

    public TokenObjectCode(TokenObject o, String redirectUri){
        super();
        this.accessToken = o.getAccessToken();
        this.expiresIn = o.getExpiresIn();
        this.refreshToken = o.getRefreshToken();
        this.tokenType = o.getTokenType();
        this.redirectUri = redirectUri;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        map.put("expires_in", expiresIn.toString());
        map.put("token_type", tokenType);
        map.put("redirect_uri",redirectUri);
        return map;
    }

    public String getRedirectUri(){
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri){
        this.redirectUri = redirectUri;
    }
}
