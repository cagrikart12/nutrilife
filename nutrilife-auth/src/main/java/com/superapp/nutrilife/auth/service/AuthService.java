package com.superapp.nutrilife.auth.service;

import com.superapp.nutrilife.auth.dto.*;
import com.superapp.nutrilife.auth.model.Role;
import com.superapp.nutrilife.auth.model.User;
import com.superapp.nutrilife.auth.repository.UserRepository;
import com.superapp.nutrilife.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Kullanıcı adı zaten kullanılıyor");
        }
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("E-posta adresi zaten kullanılıyor");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String accessToken = jwtUtil.generateToken(savedUser);
        
        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken("") // No refresh token for now
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsernameOrEmail(),
                    request.getPassword()
                )
            );

            User user = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail()
            ).orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            String accessToken = jwtUtil.generateToken(user);

            return AuthResponse.builder()
                    .token(accessToken)
                    .refreshToken("") // No refresh token for now
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().name())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Giriş başarısız: " + e.getMessage());
        }
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        // Simple implementation - just return new token
        return TokenResponse.builder()
                .accessToken("new-token")
                .refreshToken("new-refresh-token")
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .build();
    }

    public void revokeRefreshToken(String refreshToken) {
        // Simple implementation - do nothing
    }

    public void revokeAllUserTokens(String username) {
        // Simple implementation - do nothing
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String username = jwtUtil.extractUsername(token);
            
            if (jwtUtil.validateToken(token, username)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

                return TokenValidationResponse.builder()
                        .valid(true)
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .message("Token geçerli")
                        .build();
            } else {
                return TokenValidationResponse.builder()
                        .valid(false)
                        .message("Token geçersiz")
                        .build();
            }
        } catch (Exception e) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Token doğrulama hatası: " + e.getMessage())
                    .build();
        }
    }

    public void blacklistToken(String token) {
        // Simple implementation - do nothing
    }
}