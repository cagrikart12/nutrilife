package com.superapp.nutrilife.profile.controller;

import com.superapp.nutrilife.profile.dto.ProfileRequest;
import com.superapp.nutrilife.profile.dto.ProfileResponse;
import com.superapp.nutrilife.profile.model.UserProfile;
import com.superapp.nutrilife.profile.service.UserProfileService;
import com.superapp.nutrilife.profile.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Kullanıcı Profil Yönetimi", description = "Kullanıcı profillerini oluşturma, güncelleme ve yönetme işlemleri")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {
    
    private final UserProfileService userProfileService;
    private final JwtUtil jwtUtil;
    
    @PostMapping
    @Operation(summary = "Yeni profil oluştur", description = "Kullanıcı için yeni bir profil oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profil başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz veri veya profil zaten mevcut"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<ProfileResponse> createProfile(
            @Valid @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Yeni profil oluşturma isteği alındı. Kullanıcı ID: {}", userId);
        ProfileResponse response = userProfileService.createProfile(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping
    @Operation(summary = "Profil güncelle", description = "Mevcut kullanıcı profilini günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz veri"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "404", description = "Profil bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Profil güncelleme isteği alındı. Kullanıcı ID: {}", userId);
        ProfileResponse response = userProfileService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Profil getir", description = "Kullanıcı profilini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil başarıyla getirildi"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "404", description = "Profil bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<ProfileResponse> getProfile(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Profil getirme isteği alındı. Kullanıcı ID: {}", userId);
        ProfileResponse response = userProfileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/exists")
    @Operation(summary = "Profil var mı kontrol et", description = "Kullanıcının profili olup olmadığını kontrol eder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kontrol tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<Boolean> profileExists(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Profil varlık kontrolü isteği alındı. Kullanıcı ID: {}", userId);
        boolean exists = userProfileService.profileExists(userId);
        return ResponseEntity.ok(exists);
    }
    
    @DeleteMapping
    @Operation(summary = "Profil sil", description = "Kullanıcı profilini siler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profil başarıyla silindi"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "404", description = "Profil bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<Void> deleteProfile(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Profil silme isteği alındı. Kullanıcı ID: {}", userId);
        userProfileService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/all")
    @Operation(summary = "Tüm profilleri getir", description = "Sistemdeki tüm profilleri getirir (Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiller başarıyla getirildi"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "403", description = "Yetki yetersiz"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getAllProfiles(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Admin kontrolü
        if (role == null || !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        log.info("Tüm profilleri getirme isteği alındı. Kullanıcı ID: {}", userId);
        List<ProfileResponse> responses = userProfileService.getAllProfiles();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/name")
    @Operation(summary = "İsme göre profil ara", description = "Ad veya soyada göre profil arar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> searchProfilesByName(
            @Parameter(description = "Aranacak isim") @RequestParam String name,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("İsme göre profil arama isteği alındı. Arama terimi: {}, Kullanıcı ID: {}", name, userId);
        List<ProfileResponse> responses = userProfileService.searchProfilesByName(name);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/goal")
    @Operation(summary = "Hedefe göre profil ara", description = "Hedef türüne göre profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByGoal(
            @Parameter(description = "Hedef türü") @RequestParam UserProfile.Goal goal,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Hedefe göre profil arama isteği alındı. Hedef: {}, Kullanıcı ID: {}", goal, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByGoal(goal);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/activity")
    @Operation(summary = "Aktivite seviyesine göre profil ara", description = "Aktivite seviyesine göre profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByActivityLevel(
            @Parameter(description = "Aktivite seviyesi") @RequestParam UserProfile.ActivityLevel activityLevel,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Aktivite seviyesine göre profil arama isteği alındı. Seviye: {}, Kullanıcı ID: {}", activityLevel, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByActivityLevel(activityLevel);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/gender")
    @Operation(summary = "Cinsiyete göre profil ara", description = "Cinsiyete göre profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByGender(
            @Parameter(description = "Cinsiyet") @RequestParam UserProfile.Gender gender,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Cinsiyete göre profil arama isteği alındı. Cinsiyet: {}, Kullanıcı ID: {}", gender, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByGender(gender);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/age")
    @Operation(summary = "Yaş aralığına göre profil ara", description = "Belirtilen yaş aralığındaki profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByAgeRange(
            @Parameter(description = "Minimum yaş") @RequestParam int minAge,
            @Parameter(description = "Maksimum yaş") @RequestParam int maxAge,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Yaş aralığına göre profil arama isteği alındı. Aralık: {} - {}, Kullanıcı ID: {}", minAge, maxAge, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/bmi")
    @Operation(summary = "BMI aralığına göre profil ara", description = "Belirtilen BMI aralığındaki profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByBmiRange(
            @Parameter(description = "Minimum BMI") @RequestParam double minBmi,
            @Parameter(description = "Maksimum BMI") @RequestParam double maxBmi,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("BMI aralığına göre profil arama isteği alındı. Aralık: {} - {}, Kullanıcı ID: {}", minBmi, maxBmi, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByBmiRange(minBmi, maxBmi);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/calories")
    @Operation(summary = "Kalori hedefine göre profil ara", description = "Belirtilen kalori hedefi aralığındaki profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByCalorieGoalRange(
            @Parameter(description = "Minimum kalori") @RequestParam int minCalories,
            @Parameter(description = "Maksimum kalori") @RequestParam int maxCalories,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Kalori hedefine göre profil arama isteği alındı. Aralık: {} - {}, Kullanıcı ID: {}", minCalories, maxCalories, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByCalorieGoalRange(minCalories, maxCalories);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/allergy")
    @Operation(summary = "Alerjiye göre profil ara", description = "Belirtilen alerjiyi içeren profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByAllergy(
            @Parameter(description = "Alerji türü") @RequestParam String allergy,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Alerjiye göre profil arama isteği alındı. Alerji: {}, Kullanıcı ID: {}", allergy, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByAllergy(allergy);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search/dietary")
    @Operation(summary = "Diyet tercihine göre profil ara", description = "Belirtilen diyet tercihini içeren profilleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama tamamlandı"),
            @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<List<ProfileResponse>> getProfilesByDietaryPreference(
            @Parameter(description = "Diyet tercihi") @RequestParam String preference,
            HttpServletRequest httpRequest) {
        
        String token = httpRequest.getHeader("Authorization");
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Diyet tercihine göre profil arama isteği alındı. Tercih: {}, Kullanıcı ID: {}", preference, userId);
        List<ProfileResponse> responses = userProfileService.getProfilesByDietaryPreference(preference);
        return ResponseEntity.ok(responses);
    }
}
