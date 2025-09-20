package com.superapp.nutrilife.repository;

import com.superapp.nutrilife.model.Meal;
import com.superapp.nutrilife.model.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    
    List<Meal> findByUserIdAndMealDate(Long userId, LocalDate mealDate);
    
    List<Meal> findByUserIdAndMealTypeAndMealDate(Long userId, MealType mealType, LocalDate mealDate);
    
    @Query("SELECT m FROM Meal m WHERE m.userId = :userId AND m.mealDate BETWEEN :startDate AND :endDate ORDER BY m.mealDate DESC")
    List<Meal> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                       @Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);
}
