package com.superapp.nutrilife.profile.dto;

import com.superapp.nutrilife.profile.model.UserProfile;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    
    @NotBlank(message = "Ad boş olamaz")
    @Size(max = 50, message = "Ad en fazla 50 karakter olabilir")
    private String firstName;
    
    @NotBlank(message = "Soyad boş olamaz")
    @Size(max = 50, message = "Soyad en fazla 50 karakter olabilir")
    private String lastName;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Geçerli bir telefon numarası giriniz")
    private String phoneNumber;
    
    private LocalDate birthDate;
    
    private UserProfile.Gender gender;
    
    @Min(value = 50, message = "Boy en az 50 cm olmalıdır")
    @Max(value = 300, message = "Boy en fazla 300 cm olabilir")
    private Double height;
    
    @Min(value = 20, message = "Kilo en az 20 kg olmalıdır")
    @Max(value = 500, message = "Kilo en fazla 500 kg olabilir")
    private Double weight;
    
    private UserProfile.ActivityLevel activityLevel;
    
    private UserProfile.Goal goal;
    
    @Min(value = 20, message = "Hedef kilo en az 20 kg olmalıdır")
    @Max(value = 500, message = "Hedef kilo en fazla 500 kg olabilir")
    private Double targetWeight;
    
    @Min(value = 800, message = "Günlük kalori hedefi en az 800 olmalıdır")
    @Max(value = 5000, message = "Günlük kalori hedefi en fazla 5000 olabilir")
    private Integer dailyCalorieGoal;
    
    private String allergies;
    
    private String medicalConditions;
    
    private String dietaryPreferences;
    
    private String profilePictureUrl;
    
    @Size(max = 500, message = "Biyografi en fazla 500 karakter olabilir")
    private String bio;
}
