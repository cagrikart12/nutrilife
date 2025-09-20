package com.superapp.nutrilife.controller;

import com.superapp.nutrilife.dto.FoodRequest;
import com.superapp.nutrilife.dto.FoodResponse;
import com.superapp.nutrilife.model.enums.FoodSource;
import com.superapp.nutrilife.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/core")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Food Management", description = "Yemek yönetimi işlemleri")
public class FoodController {
    
    private final FoodService foodService;
    
    @Operation(summary = "Yemek oluştur", description = "Yeni yemek kaydı oluşturur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Yemek başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz veri")
    })
    @PostMapping("/foods")
    public ResponseEntity<?> createFood(@Valid @RequestBody FoodRequest request) {
        try {
            FoodResponse response = foodService.createFood(request);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @Operation(summary = "Tüm yemekleri listele", description = "Sistemdeki tüm yemekleri listeler")
    @ApiResponse(responseCode = "200", description = "Yemekler başarıyla listelendi")
    @GetMapping("/foods")
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        List<FoodResponse> foods = foodService.getAllFoods();
        return ResponseEntity.ok(foods);
    }
    
    @Operation(summary = "Yemek detayı", description = "ID'ye göre yemek detayını getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Yemek bulundu"),
        @ApiResponse(responseCode = "404", description = "Yemek bulunamadı")
    })
    @GetMapping("/foods/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Long id) {
        try {
            FoodResponse response = foodService.getFoodById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Yemek ara", description = "İsim veya açıklamaya göre yemek arar")
    @ApiResponse(responseCode = "200", description = "Arama tamamlandı")
    @GetMapping("/foods/search")
    public ResponseEntity<List<FoodResponse>> searchFoods(@RequestParam String q) {
        List<FoodResponse> foods = foodService.searchFoods(q);
        return ResponseEntity.ok(foods);
    }
    
    @Operation(summary = "Kaynağa göre yemekler", description = "Belirli kaynaktan yemekleri listeler")
    @ApiResponse(responseCode = "200", description = "Yemekler listelendi")
    @GetMapping("/foods/source/{source}")
    public ResponseEntity<List<FoodResponse>> getFoodsBySource(@PathVariable FoodSource source) {
        List<FoodResponse> foods = foodService.getFoodsBySource(source);
        return ResponseEntity.ok(foods);
    }
    
    @Operation(summary = "Kalori aralığına göre yemekler", description = "Belirli kalori aralığındaki yemekleri listeler")
    @ApiResponse(responseCode = "200", description = "Yemekler listelendi")
    @GetMapping("/foods/calories")
    public ResponseEntity<List<FoodResponse>> getFoodsByCaloriesRange(
            @RequestParam Double min, 
            @RequestParam Double max) {
        List<FoodResponse> foods = foodService.getFoodsByCaloriesRange(min, max);
        return ResponseEntity.ok(foods);
    }
    
    @Operation(summary = "Yemek güncelle", description = "Mevcut yemek bilgilerini günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Yemek başarıyla güncellendi"),
        @ApiResponse(responseCode = "404", description = "Yemek bulunamadı")
    })
    @PutMapping("/foods/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @Valid @RequestBody FoodRequest request) {
        try {
            FoodResponse response = foodService.updateFood(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Yemek sil", description = "Yemek kaydını siler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Yemek başarıyla silindi"),
        @ApiResponse(responseCode = "404", description = "Yemek bulunamadı")
    })
    @DeleteMapping("/foods/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
            return ResponseEntity.ok(Map.of("message", "Yemek başarıyla silindi"));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
