package com.superapp.nutrilife.repository;

import com.superapp.nutrilife.model.Food;
import com.superapp.nutrilife.model.enums.FoodSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    List<Food> findByNameContainingIgnoreCase(String name);
    
    List<Food> findBySource(FoodSource source);
    
    @Query("SELECT f FROM Food f WHERE f.name ILIKE %:name% OR f.description ILIKE %:name%")
    List<Food> searchByNameOrDescription(@Param("name") String searchTerm);
    
    @Query("SELECT f FROM Food f WHERE f.calories BETWEEN :minCalories AND :maxCalories")
    List<Food> findByCaloriesRange(@Param("minCalories") Double minCalories, @Param("maxCalories") Double maxCalories);
}
