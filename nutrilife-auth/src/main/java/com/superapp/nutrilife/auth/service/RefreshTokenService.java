package com.superapp.nutrilife.auth.service;

import com.superapp.nutrilife.auth.model.RefreshToken;
import com.superapp.nutrilife.auth.model.User;
import com.superapp.nutrilife.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    
    public RefreshToken createRefreshToken(User user) {
        // Önce kullanıcının eski token'larını temizle
        refreshTokenRepository.revokeAllUserTokens(user);
        
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7)) // 7 gün geçerli
                .revoked(false)
                .build();
        
        return refreshTokenRepository.save(refreshToken);
    }
    
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token bulunamadı"));
    }
    
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token süresi dolmuş");
        }
        return token;
    }
    
    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        return verifyExpiration(refreshToken);
    }
    
    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }
    
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);
    }
    
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}