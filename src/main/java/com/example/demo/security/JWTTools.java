package com.example.demo.security;

import com.example.demo.entities.User;
import com.example.demo.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    //1 convrto la stringa secret in una chiave HMAC valida per firmare il JWT.
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        // prendo il tempo corrente in millisecondi
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis())) //data di emissione
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 giorni
                .setSubject(String.valueOf(user.getId())) //subject a chi apaprtiene il token mettendo id utente
                .signWith(getKey()) //firmo il server con la chiave segreta. se viene modificato da qualcuno, salta
                // cioè mi da poi signwithgetkey
                .compact(); // compatto tutto in una stringa header payload signature.
    }

    public void verifyToken(String token) {
        try {
            // 1 parsing controlla firma e scadenza
            //2 se qualcosa non va fa eccezione
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token); // valida firma + scadenza
        } catch (Exception ex) {
            //token non valido o scaduto quindi errore 401
            throw new UnauthorizedException("Problemi con il token, rifai il login");
        }
    }

    public Long extractIdFromToken(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}