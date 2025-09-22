package com.superapp.nutrilife.repository;

import com.superapp.nutrilife.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    /**
     * Kullanıcı ID'sine göre profil bulur
     */
    Optional<UserProfile> findByUserId(Long userId);
    
    /**
     * Kullanıcı ID'sine göre profil var mı kontrol eder
     */
    boolean existsByUserId(Long userId);
    
    /**
     * Ad veya soyada göre profil arar
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<UserProfile> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Hedef türüne göre profilleri getirir
     */
    List<UserProfile> findByGoal(UserProfile.Goal goal);
    
    /**
     * Aktivite seviyesine göre profilleri getirir
     */
    List<UserProfile> findByActivityLevel(UserProfile.ActivityLevel activityLevel);
    
    /**
     * Cinsiyete göre profilleri getirir
     */
    List<UserProfile> findByGender(UserProfile.Gender gender);
    
    /**
     * Yaş aralığına göre profilleri getirir
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(p.birthDate) BETWEEN :minAge AND :maxAge")
    List<UserProfile> findByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    /**
     * BMI aralığına göre profilleri getirir
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "p.height IS NOT NULL AND p.weight IS NOT NULL AND " +
           "(p.weight / POWER(p.height / 100.0, 2)) BETWEEN :minBmi AND :maxBmi")
    List<UserProfile> findByBmiRange(@Param("minBmi") double minBmi, @Param("maxBmi") double maxBmi);
    
    /**
     * Günlük kalori hedefine göre profilleri getirir
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "p.dailyCalorieGoal BETWEEN :minCalories AND :maxCalories")
    List<UserProfile> findByCalorieGoalRange(@Param("minCalories") int minCalories, @Param("maxCalories") int maxCalories);
    
    /**
     * Alerji içeren profilleri getirir
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "LOWER(p.allergies) LIKE LOWER(CONCAT('%', :allergy, '%'))")
    List<UserProfile> findByAllergyContaining(@Param("allergy") String allergy);
    
    /**
     * Diyet tercihi içeren profilleri getirir
     */
    @Query("SELECT p FROM UserProfile p WHERE " +
           "LOWER(p.dietaryPreferences) LIKE LOWER(CONCAT('%', :preference, '%'))")
    List<UserProfile> findByDietaryPreferenceContaining(@Param("preference") String preference);
}
