package com.superapp.nutrilife.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Kullanıcı adı veya e-posta boş olamaz")
    private String usernameOrEmail;
    
    @NotBlank(message = "Parola boş olamaz")
    private String password;
}
