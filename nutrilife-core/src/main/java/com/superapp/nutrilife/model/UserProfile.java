package com.superapp.nutrilife.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @NotBlank(message = "Ad boş olamaz")
    @Size(max = 50, message = "Ad en fazla 50 karakter olabilir")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank(message = "Soyad boş olamaz")
    @Size(max = 50, message = "Soyad en fazla 50 karakter olabilir")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "phone_number")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Geçerli bir telefon numarası giriniz")
    private String phoneNumber;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    
    @Column(name = "height")
    @Min(value = 50, message = "Boy en az 50 cm olmalıdır")
    @Max(value = 300, message = "Boy en fazla 300 cm olabilir")
    private Double height; // cm cinsinden
    
    @Column(name = "weight")
    @Min(value = 20, message = "Kilo en az 20 kg olmalıdır")
    @Max(value = 500, message = "Kilo en fazla 500 kg olabilir")
    private Double weight; // kg cinsinden
    
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level")
    private ActivityLevel activityLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "goal")
    private Goal goal;
    
    @Column(name = "target_weight")
    @Min(value = 20, message = "Hedef kilo en az 20 kg olmalıdır")
    @Max(value = 500, message = "Hedef kilo en fazla 500 kg olabilir")
    private Double targetWeight; // kg cinsinden
    
    @Column(name = "daily_calorie_goal")
    @Min(value = 800, message = "Günlük kalori hedefi en az 800 olmalıdır")
    @Max(value = 5000, message = "Günlük kalori hedefi en fazla 5000 olabilir")
    private Integer dailyCalorieGoal;
    
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies; // Virgülle ayrılmış alerji listesi
    
    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions; // Virgülle ayrılmış tıbbi durum listesi
    
    @Column(name = "dietary_preferences", columnDefinition = "TEXT")
    private String dietaryPreferences; // Virgülle ayrılmış diyet tercihleri
    
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    
    @Column(name = "bio", columnDefinition = "TEXT")
    @Size(max = 500, message = "Biyografi en fazla 500 karakter olabilir")
    private String bio;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Enum tanımları
    public enum Gender {
        MALE("Erkek"),
        FEMALE("Kadın"),
        OTHER("Diğer");
        
        private final String displayName;
        
        Gender(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum ActivityLevel {
        SEDENTARY("Hareketsiz", 1.2),
        LIGHTLY_ACTIVE("Hafif Aktif", 1.375),
        MODERATELY_ACTIVE("Orta Aktif", 1.55),
        VERY_ACTIVE("Çok Aktif", 1.725),
        EXTRA_ACTIVE("Aşırı Aktif", 1.9);
        
        private final String displayName;
        private final double multiplier;
        
        ActivityLevel(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public double getMultiplier() {
            return multiplier;
        }
    }
    
    public enum Goal {
        WEIGHT_LOSS("Kilo Verme"),
        WEIGHT_GAIN("Kilo Alma"),
        WEIGHT_MAINTENANCE("Kilo Koruma"),
        MUSCLE_GAIN("Kas Kazanma"),
        GENERAL_HEALTH("Genel Sağlık");
        
        private final String displayName;
        
        Goal(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
