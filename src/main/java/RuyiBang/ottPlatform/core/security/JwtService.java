package RuyiBang.ottPlatform.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    @Value("${security.token.secret}")
    private String secret;

    @Value("${security.token.expiry-minutes:60}")
    private long expiryMinutes;

    private SecretKey key;

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT token for a user or service client.
     *
     * @param subject the unique identifier (e.g. username, clientId)
     * @param roles list of roles or permissions assigned to the subject
     * @param extraClaims optional custom claims like email, tenantId, etc.
     * @return signed JWT token
     */
    public String generateToken(String subject, List<String> roles, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>();
        if (extraClaims != null) {
            claims.putAll(extraClaims);
        }
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate the JWT token and return the claims if valid.
     */
    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

    /**
     * Extract the username or clientId from the token.
     */
    public String getSubject(String token) {
        return validateToken(token).getBody().getSubject();
    }

    /**
     * Extract roles from the token.
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Object roles = validateToken(token).getBody().get("roles");
        if (roles instanceof List<?>) {
            return (List<String>) roles;
        }
        return Collections.emptyList();
    }

    /**
     * Check if a token has a specific role.
     */
    public boolean hasRole(String token, String role) {
        return getRoles(token).contains(role);
    }
}
