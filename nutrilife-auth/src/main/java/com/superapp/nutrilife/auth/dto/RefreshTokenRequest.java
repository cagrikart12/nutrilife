package com.superapp.nutrilife.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token boş olamaz")
    private String refreshToken;
}
