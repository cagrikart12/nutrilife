package com.superapp.nutrilife.profile.dto;

import com.superapp.nutrilife.profile.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private UserProfile.Gender gender;
    private Double height;
    private Double weight;
    private UserProfile.ActivityLevel activityLevel;
    private UserProfile.Goal goal;
    private Double targetWeight;
    private Integer dailyCalorieGoal;
    private String allergies;
    private String medicalConditions;
    private String dietaryPreferences;
    private String profilePictureUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Hesaplanmış alanlar
    private Integer age;
    private Double bmi;
    private String bmiCategory;
    private Integer bmr; // Bazal Metabolizma Hızı
    private Integer tdee; // Toplam Günlük Enerji Harcaması
    
    public static ProfileResponse fromUserProfile(UserProfile profile) {
        ProfileResponseBuilder builder = ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .birthDate(profile.getBirthDate())
                .gender(profile.getGender())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .activityLevel(profile.getActivityLevel())
                .goal(profile.getGoal())
                .targetWeight(profile.getTargetWeight())
                .dailyCalorieGoal(profile.getDailyCalorieGoal())
                .allergies(profile.getAllergies())
                .medicalConditions(profile.getMedicalConditions())
                .dietaryPreferences(profile.getDietaryPreferences())
                .profilePictureUrl(profile.getProfilePictureUrl())
                .bio(profile.getBio())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt());
        
        // Yaş hesaplama
        if (profile.getBirthDate() != null) {
            LocalDate now = LocalDate.now();
            int age = now.getYear() - profile.getBirthDate().getYear();
            if (now.getDayOfYear() < profile.getBirthDate().getDayOfYear()) {
                age--;
            }
            builder.age(age);
        }
        
        // BMI hesaplama
        if (profile.getHeight() != null && profile.getWeight() != null) {
            double heightInMeters = profile.getHeight() / 100.0;
            double bmi = profile.getWeight() / (heightInMeters * heightInMeters);
            builder.bmi(Math.round(bmi * 10.0) / 10.0);
            
            // BMI kategorisi
            String bmiCategory;
            if (bmi < 18.5) {
                bmiCategory = "Zayıf";
            } else if (bmi < 25) {
                bmiCategory = "Normal";
            } else if (bmi < 30) {
                bmiCategory = "Fazla Kilolu";
            } else {
                bmiCategory = "Obez";
            }
            builder.bmiCategory(bmiCategory);
        }
        
        // BMR hesaplama (Mifflin-St Jeor formülü)
        if (profile.getHeight() != null && profile.getWeight() != null && profile.getGender() != null) {
            double heightInCm = profile.getHeight();
            double weightInKg = profile.getWeight();
            int age = builder.build().getAge() != null ? builder.build().getAge() : 30;
            
            double bmr;
            if (profile.getGender() == UserProfile.Gender.MALE) {
                bmr = (10 * weightInKg) + (6.25 * heightInCm) - (5 * age) + 5;
            } else {
                bmr = (10 * weightInKg) + (6.25 * heightInCm) - (5 * age) - 161;
            }
            builder.bmr((int) Math.round(bmr));
            
            // TDEE hesaplama
            if (profile.getActivityLevel() != null) {
                double tdee = bmr * profile.getActivityLevel().getMultiplier();
                builder.tdee((int) Math.round(tdee));
            }
        }
        
        return builder.build();
    }
}
