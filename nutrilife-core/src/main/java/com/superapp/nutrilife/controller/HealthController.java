package com.superapp.nutrilife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/core")
@Tag(name = "Core Service", description = "NutriLife Core mikroservis işlemleri")
public class HealthController {
    
    @Operation(summary = "Core servis durumu", description = "Core mikroservisinin çalışıp çalışmadığını kontrol eder")
    @ApiResponse(responseCode = "200", description = "Core servis çalışıyor")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Core service is running");
    }
    
    @Operation(summary = "Servis bilgileri", description = "Core servisinin detaylı bilgilerini döner")
    @ApiResponse(responseCode = "200", description = "Servis bilgileri başarıyla alındı")
    @GetMapping("/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("NutriLife Core Service v1.0.0 - Port: 8080");
    }
}
