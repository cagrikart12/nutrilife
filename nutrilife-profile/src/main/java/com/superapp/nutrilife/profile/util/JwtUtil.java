package com.superapp.nutrilife.profile.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    
    @Value("${jwt.secret:nutrilife-secret-key-for-profile-service-very-long-and-secure}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}")
    private int jwtExpirationMs;
    
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * JWT token'dan kullanıcı ID'sini çıkarır
     */
    public Long getUserIdFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            log.error("JWT token'dan kullanıcı ID'si çıkarılamadı: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * JWT token'dan kullanıcı adını çıkarır
     */
    public String getUsernameFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.error("JWT token'dan kullanıcı adı çıkarılamadı: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * JWT token'dan e-posta adresini çıkarır (artık token'da yok)
     */
    public String getEmailFromToken(String token) {
        // Email artık token'da saklanmıyor, null döndür
        return null;
    }
    
    /**
     * JWT token'dan rol bilgisini çıkarır
     */
    public String getRoleFromToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.get("role", String.class);
        } catch (Exception e) {
            log.error("JWT token'dan rol bilgisi çıkarılamadı: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Token'ın geçerli olup olmadığını kontrol eder
     */
    public boolean validateToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            
            return true;
        } catch (Exception e) {
            log.error("JWT token geçersiz: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Token'ın süresi dolmuş mu kontrol eder
     */
    public boolean isTokenExpired(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token süre kontrolü yapılamadı: {}", e.getMessage());
            return true;
        }
    }
    
    /**
     * Security context'ten kullanıcı ID'sini alır
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                return Long.valueOf(authentication.getName());
            } catch (NumberFormatException e) {
                log.error("Authentication'dan kullanıcı ID'si çıkarılamadı: {}", authentication.getName());
                return null;
            }
        }
        return null;
    }
    
    /**
     * Security context'ten kullanıcı adını alır
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
