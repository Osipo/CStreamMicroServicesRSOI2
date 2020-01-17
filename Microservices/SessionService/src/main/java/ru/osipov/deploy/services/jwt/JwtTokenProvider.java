package ru.osipov.deploy.services.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.osipov.deploy.entities.Role;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.exceptions.JwtAuthenticationException;
import ru.osipov.deploy.models.TokenObject;
import ru.osipov.deploy.models.user.UserModel;
import ru.osipov.deploy.services.oauth.OAuthTokenService;
import ru.osipov.deploy.entities.oauth.OAuthClient;
import ru.osipov.deploy.entities.oauth.OAuthToken;
import ru.osipov.deploy.services.UserService;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.*;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private String secretKey = "d282dc035756736e54761761cc52bef78e3c473fa7de8f617c14f0e0ae7044aae8ba4b7bed7d532d4af91122e50b39a8bb99e320f72094547d7cae108e928460";

    @Value("${jwt.access_token.expired}")
    private long validityAccessTokenInMilliseconds;

    @Value("${jwt.refresh_token.expired}")
    private long validityRefreshTokenInMilliseconds;


    //@Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
    @Autowired
    private OAuthTokenService oAuthTokenService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public TokenObject createToken(UserEntity user, OAuthClient oAuthClient) {
        Claims claimsAccess = Jwts.claims().setSubject(user.getUsername());
        claimsAccess.put("roles", getRoleNames(user.getRoles()));
        claimsAccess.put("client_id", oAuthClient.getClientId());
        claimsAccess.put("user_id", user.getId());
        claimsAccess.put("username", user.getUsername());
        OAuthToken token = oAuthTokenService.findByUserId(user.getId());
        try {
            String accessToken = generationToken(claimsAccess, validityAccessTokenInMilliseconds);

            Claims claimsRefresh = Jwts.claims().setSubject(user.getId().toString());
            claimsRefresh.put("user_id", user.getId());

            TokenObject tokenObject = new TokenObject();
            tokenObject.setAccessToken(accessToken);
            tokenObject.setExpiresIn(validityAccessTokenInMilliseconds);
            tokenObject.setTokenType("bearer");

            String refreshToken = generationToken(claimsRefresh, validityRefreshTokenInMilliseconds);
            if (token == null) {
                OAuthToken oAuthToken = new OAuthToken();
                oAuthToken.setAccessToken(accessToken);
                oAuthToken.setAccessTokenValidity(true);
                oAuthToken.setClientId(oAuthClient.getClientId());
                oAuthToken.setRefreshToken(refreshToken);
                oAuthToken.setRefreshTokenValidity(true);
                oAuthToken.setUserId(user.getId());

                tokenObject.setRefreshToken(refreshToken);
                oAuthTokenService.create(oAuthToken);
            } else {
                token.setAccessToken(accessToken);
                token.setAccessTokenValidity(true);
                token.setClientId(oAuthClient.getClientId());
                token.setUserId(user.getId());
                if (token.isRefreshTokenValidity()) {
                    tokenObject.setRefreshToken(token.getRefreshToken());
                }
                else {
                    token.setRefreshToken(refreshToken);
                    tokenObject.setRefreshToken(refreshToken);
                }
                token.setRefreshTokenValidity(true);

                oAuthTokenService.update(token);
            }
            return tokenObject;

        } catch (Exception exc) {
            throw new JwtAuthenticationException(exc.getMessage());
        }
    }

    public TokenObject updateToken(String refreshToken, UUID oAuthClientId) {
        Jws<Claims> refreshClaims = getJwsClaimsFromToken(refreshToken);
        Long userId = Long.parseLong(refreshClaims.getBody().get("user_id", String.class));
        OAuthToken oAuthToken = oAuthTokenService.findByUserId(userId);
        if (oAuthToken == null)
            throw new BadCredentialsException("Invalid token");
        oAuthTokenService.deleteByUserId(oAuthToken.getUserId());
        if (!oAuthToken.getClientId().equals(oAuthClientId))
            throw new BadCredentialsException("Invalid clientId");
        Jws<Claims> accessClaims = getJwsClaimsFromToken(oAuthToken.getAccessToken());

        try {
            String accessToken = generationToken(accessClaims.getBody(), validityAccessTokenInMilliseconds);
            Claims claimsRefresh = refreshClaims.getBody();
            TokenObject tokenObject = new TokenObject();
            tokenObject.setAccessToken(accessToken);
            tokenObject.setExpiresIn(validityAccessTokenInMilliseconds);
            tokenObject.setTokenType("bearer");
            String newRefreshToken = generationToken(claimsRefresh, validityRefreshTokenInMilliseconds);
            tokenObject.setRefreshToken(newRefreshToken);

            oAuthToken.setUserId(null);
            oAuthToken.setAccessToken(accessToken);
            oAuthToken.setAccessTokenValidity(true);
            oAuthToken.setRefreshToken(newRefreshToken);
            oAuthToken.setRefreshTokenValidity(true);
            oAuthTokenService.create(oAuthToken);

            return tokenObject;
        } catch (Exception exc) {
            throw new JwtAuthenticationException(exc.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public TokenObject getTokenObjectByToken(String token) {
        Claims claims = getJwsClaimsFromToken(token).getBody();
        Long userId = Long.parseLong(claims.get("user_id", String.class));
        OAuthToken oAuthToken = oAuthTokenService.findByUserId(userId);

        TokenObject tokenObject = new TokenObject();
        tokenObject.setRefreshToken(oAuthToken.getRefreshToken());
        tokenObject.setExpiresIn(validityAccessTokenInMilliseconds);
        tokenObject.setTokenType("bearer");
        tokenObject.setAccessToken(oAuthToken.getAccessToken());

        return tokenObject;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claims = getJwsClaimsFromToken(token);
            Long userId = Long.parseLong(claims.getBody().get("user_id", String.class));
            Date date = claims.getBody().getExpiration();
            OAuthToken oAuthToken = oAuthTokenService.findByUserId(userId);
            if (oAuthToken == null || !oAuthToken.getAccessToken().equals(token))
                return false;
            if (date.before(new Date())) {
                logger.warn("JWT Token is expired.");
                oAuthToken.setAccessTokenValidity(false);
                oAuthTokenService.update(oAuthToken);
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException exc) {
            if (exc instanceof ExpiredJwtException) {
                logger.warn("JWT Token is expired.");
            } else {
                logger.warn("JWT Token is invalid.");
            }
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = getJwsClaimsFromToken(token);
            Long userId = Long.parseLong(claims.getBody().get("user_id", String.class));
            Date date = claims.getBody().getExpiration();
            OAuthToken oAuthToken = oAuthTokenService.findByUserId(userId);
            if (oAuthToken == null || !oAuthToken.getRefreshToken().equals(token))
                return false;
            if (date.before(new Date())) {
                logger.warn("JWT Token is expired.");
                oAuthToken.setRefreshTokenValidity(false);
                oAuthTokenService.update(oAuthToken);
            }
            return oAuthToken.isRefreshTokenValidity();
        } catch (JwtException | IllegalArgumentException exc) {
            if (exc instanceof ExpiredJwtException) {
                logger.warn("JWT Token is expired.");
            } else {
                logger.warn("JWT Token is invalid.");
            }
            return false;
        }
    }

    public UserModel getUserModelByToken(String token) {
        Jws<Claims> claims = getJwsClaimsFromToken(token);
        Long clientId = Long.parseLong(claims.getBody().get("user_id", String.class));
        return userService.findById(clientId);
    }

    public Long getUserIdByToken(String token) {
        Jws<Claims> claims = getJwsClaimsFromToken(token);
        return Long.parseLong(claims.getBody().get("user_id", String.class));
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getName().toString());
        });

        return result;
    }

    private String generationToken(Claims claims, long validityMilliseconds) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secretKey);
        return builder.compact();
    }

    private Jws<Claims> getJwsClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }
}