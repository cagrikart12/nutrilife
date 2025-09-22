package com.superapp.nutrilife.service;

import com.superapp.nutrilife.dto.ProfileRequest;
import com.superapp.nutrilife.dto.ProfileResponse;
import com.superapp.nutrilife.model.UserProfile;
import com.superapp.nutrilife.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    
    /**
     * Yeni profil oluşturur
     */
    public ProfileResponse createProfile(Long userId, ProfileRequest request) {
        log.info("Kullanıcı {} için yeni profil oluşturuluyor", userId);
        
        // Kullanıcının zaten profili var mı kontrol et
        if (userProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("Bu kullanıcının zaten bir profili bulunmaktadır");
        }
        
        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .height(request.getHeight())
                .weight(request.getWeight())
                .activityLevel(request.getActivityLevel())
                .goal(request.getGoal())
                .targetWeight(request.getTargetWeight())
                .dailyCalorieGoal(request.getDailyCalorieGoal())
                .allergies(request.getAllergies())
                .medicalConditions(request.getMedicalConditions())
                .dietaryPreferences(request.getDietaryPreferences())
                .profilePictureUrl(request.getProfilePictureUrl())
                .bio(request.getBio())
                .build();
        
        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Profil başarıyla oluşturuldu. Profil ID: {}", savedProfile.getId());
        
        return ProfileResponse.fromUserProfile(savedProfile);
    }
    
    /**
     * Profil günceller
     */
    public ProfileResponse updateProfile(Long userId, ProfileRequest request) {
        log.info("Kullanıcı {} için profil güncelleniyor", userId);
        
        UserProfile existingProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı profili bulunamadı"));
        
        // Profil bilgilerini güncelle
        existingProfile.setFirstName(request.getFirstName());
        existingProfile.setLastName(request.getLastName());
        existingProfile.setPhoneNumber(request.getPhoneNumber());
        existingProfile.setBirthDate(request.getBirthDate());
        existingProfile.setGender(request.getGender());
        existingProfile.setHeight(request.getHeight());
        existingProfile.setWeight(request.getWeight());
        existingProfile.setActivityLevel(request.getActivityLevel());
        existingProfile.setGoal(request.getGoal());
        existingProfile.setTargetWeight(request.getTargetWeight());
        existingProfile.setDailyCalorieGoal(request.getDailyCalorieGoal());
        existingProfile.setAllergies(request.getAllergies());
        existingProfile.setMedicalConditions(request.getMedicalConditions());
        existingProfile.setDietaryPreferences(request.getDietaryPreferences());
        existingProfile.setProfilePictureUrl(request.getProfilePictureUrl());
        existingProfile.setBio(request.getBio());
        
        UserProfile updatedProfile = userProfileRepository.save(existingProfile);
        log.info("Profil başarıyla güncellendi. Profil ID: {}", updatedProfile.getId());
        
        return ProfileResponse.fromUserProfile(updatedProfile);
    }
    
    /**
     * Kullanıcı profilini getirir
     */
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {
        log.info("Kullanıcı {} için profil getiriliyor", userId);
        
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı profili bulunamadı"));
        
        return ProfileResponse.fromUserProfile(profile);
    }
    
    /**
     * Profil var mı kontrol eder
     */
    @Transactional(readOnly = true)
    public boolean profileExists(Long userId) {
        return userProfileRepository.existsByUserId(userId);
    }
    
    /**
     * Profili siler
     */
    public void deleteProfile(Long userId) {
        log.info("Kullanıcı {} için profil siliniyor", userId);
        
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı profili bulunamadı"));
        
        userProfileRepository.delete(profile);
        log.info("Profil başarıyla silindi. Profil ID: {}", profile.getId());
    }
    
    /**
     * Tüm profilleri getirir (admin için)
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getAllProfiles() {
        log.info("Tüm profiller getiriliyor");
        
        return userProfileRepository.findAll().stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * İsme göre profil arar
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> searchProfilesByName(String name) {
        log.info("İsim ile profil aranıyor: {}", name);
        
        return userProfileRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Hedef türüne göre profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByGoal(UserProfile.Goal goal) {
        log.info("Hedef türüne göre profiller getiriliyor: {}", goal);
        
        return userProfileRepository.findByGoal(goal).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Aktivite seviyesine göre profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByActivityLevel(UserProfile.ActivityLevel activityLevel) {
        log.info("Aktivite seviyesine göre profiller getiriliyor: {}", activityLevel);
        
        return userProfileRepository.findByActivityLevel(activityLevel).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Cinsiyete göre profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByGender(UserProfile.Gender gender) {
        log.info("Cinsiyete göre profiller getiriliyor: {}", gender);
        
        return userProfileRepository.findByGender(gender).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Yaş aralığına göre profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByAgeRange(int minAge, int maxAge) {
        log.info("Yaş aralığına göre profiller getiriliyor: {} - {}", minAge, maxAge);
        
        return userProfileRepository.findByAgeRange(minAge, maxAge).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * BMI aralığına göre profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByBmiRange(double minBmi, double maxBmi) {
        log.info("BMI aralığına göre profiller getiriliyor: {} - {}", minBmi, maxBmi);
        
        return userProfileRepository.findByBmiRange(minBmi, maxBmi).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Kalori hedefine göre profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByCalorieGoalRange(int minCalories, int maxCalories) {
        log.info("Kalori hedefine göre profiller getiriliyor: {} - {}", minCalories, maxCalories);
        
        return userProfileRepository.findByCalorieGoalRange(minCalories, maxCalories).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Alerji içeren profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByAllergy(String allergy) {
        log.info("Alerji ile profil aranıyor: {}", allergy);
        
        return userProfileRepository.findByAllergyContaining(allergy).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
    
    /**
     * Diyet tercihi içeren profilleri getirir
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfilesByDietaryPreference(String preference) {
        log.info("Diyet tercihi ile profil aranıyor: {}", preference);
        
        return userProfileRepository.findByDietaryPreferenceContaining(preference).stream()
                .map(ProfileResponse::fromUserProfile)
                .collect(Collectors.toList());
    }
}
