// auth/JwtService.java
package ar.utn.tup.goblinmaster.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Falta la propiedad app.jwt.secret");
        }
        // Si el secreto parece Base64, lo decodificamos; si no, usamos bytes directos.
        byte[] keyBytes;
        try {
            // Heurística simple: Base64 suele ser largo y solo [A-Za-z0-9+/=]
            boolean looksBase64 = secret.matches("^[A-Za-z0-9+/=]+$") && secret.length() % 4 == 0;
            keyBytes = looksBase64 ? io.jsonwebtoken.io.Decoders.BASE64.decode(secret) : secret.getBytes();
        } catch (Exception ex) {
            throw new IllegalStateException("Secreto JWT inválido (no se pudo decodificar)", ex);
        }
        if (keyBytes.length < 32) { // 256 bits
            throw new IllegalStateException("app.jwt.secret es demasiado corto: se requieren al menos 256 bits (32 bytes).");
        }
        this.key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            return extractUsername(token).equals(user.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
