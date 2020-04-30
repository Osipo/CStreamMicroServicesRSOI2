package ru.osipov.deploy.configuration.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.osipov.deploy.WebConfig;
import ru.osipov.deploy.exception.jwt.JwtAuthenticationException;
import ru.osipov.deploy.models.TokenObject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component("jwtTokenProvider")
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private String secretKey = "d282dc035756736e54761761cc52bef78e3c473fa7de8f617c14f0e0ae7044aae8ba4b7bed7d532d4af91122e50b39a8bb99e320f72094547d7cae108e928460";

    @Value("${jwt.token.expires}")
    private long validityTokenMilliseconds;

    public TokenObject getToken(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Basic ")) {
            String basic = auth.substring(6);
            String base64 = String.format("base64(%s:%s)", WebConfig.getGatewayKey(), WebConfig.getGatewaySecret());
            if (basic.replaceAll(" ", "").contains(base64)) {
                Claims claims = Jwts.claims().setSubject(WebConfig.getGatewayKey());
                claims.put("date", new Date());
                claims.put("app_id", WebConfig.getGatewayKey());
                claims.put("name_service", "SeanceService");

                String accessToken = generationToken(claims);

                TokenObject token = new TokenObject();
                token.setAccessToken(accessToken);
                token.setExpiresIn(validityTokenMilliseconds);
                token.setTokenType("basic");
                return token;
            }
        }
        throw new JwtAuthenticationException("Header is invalid");
    }

    private String generationToken(Claims claims) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS512, secretKey);
        return builder.compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Basic ")) {
            return bearerToken.substring(6);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getJwsClaimsFromToken(token);
            Date date = new Date(claims.getBody().get("date", Date.class).getTime() + validityTokenMilliseconds);
            String appId = getAppId(token);
            String nameService = claims.getBody().get("name_service", String.class);
            if (!appId.equals(WebConfig.getGatewayKey()) || !nameService.equals("SeanceService")) {
                logger.warn("JWT Token is invalid.");
                return false;
            }
            if (date.before(new Date())) {
                logger.warn("JWT Token is expired.");
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

    private Jws<Claims> getJwsClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    private String getAppId(String token) {
        Jws<Claims> claims = getJwsClaimsFromToken(token);
        return claims.getBody().get("app_id", String.class);
    }

    Authentication getAuthentication(String token) {
        Jws<Claims> claims = getJwsClaimsFromToken(token);
        String appId = claims.getBody().get("app_id", String.class);
        if (appId.equals(WebConfig.getGatewayKey())) {
            UserDetails userDetails = new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add((GrantedAuthority) () -> "ADMIN");
                    return authorities;
                }

                @Override
                public String getPassword() {
                    return WebConfig.getGatewaySecret();
                }

                @Override
                public String getUsername() {
                    return WebConfig.getGatewayKey();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        throw new JwtAuthenticationException("Invalid authentication");
    }
}
