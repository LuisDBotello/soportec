package soportec.demo.utilidades;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

public class JwtUtil {

    @Value("${jwt.secret}")
    private static String secretKey;
    
    @Value("${jwt.expiration}")
    private static long expiration;
    // Generar token
    public String generarToken(String username, String nivelPrivilegio) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nivelPrivilegio", nivelPrivilegio)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    // Extraer username
    public static String getUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // Extraer nivel de privilegio
    public static String getNivelPrivilegio(String token) {
        return extraerClaims(token).get("nivelPrivilegio", String.class);
    }

    // Validar token
    public static boolean esValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
