package com.superapp.nutrilife.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenValidationRequest {
    @NotBlank(message = "Token boş olamaz")
    private String token;
}
