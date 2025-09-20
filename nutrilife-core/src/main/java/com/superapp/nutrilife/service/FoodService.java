package com.superapp.nutrilife.service;

import com.superapp.nutrilife.dto.FoodRequest;
import com.superapp.nutrilife.dto.FoodResponse;
import com.superapp.nutrilife.model.Food;
import com.superapp.nutrilife.model.enums.FoodSource;
import com.superapp.nutrilife.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    
    private final FoodRepository foodRepository;
    
    @Transactional
    public FoodResponse createFood(FoodRequest request) {
        Food food = new Food();
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setCalories(request.getCalories());
        food.setProtein(request.getProtein());
        food.setCarbohydrates(request.getCarbohydrates());
        food.setFat(request.getFat());
        food.setFiber(request.getFiber());
        food.setSource(request.getSource());
        
        Food savedFood = foodRepository.save(food);
        return mapToResponse(savedFood);
    }
    
    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public FoodResponse getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yemek bulunamadı"));
        return mapToResponse(food);
    }
    
    public List<FoodResponse> searchFoods(String searchTerm) {
        return foodRepository.searchByNameOrDescription(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<FoodResponse> getFoodsBySource(FoodSource source) {
        return foodRepository.findBySource(source).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<FoodResponse> getFoodsByCaloriesRange(Double minCalories, Double maxCalories) {
        return foodRepository.findByCaloriesRange(minCalories, maxCalories).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public FoodResponse updateFood(Long id, FoodRequest request) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yemek bulunamadı"));
        
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setCalories(request.getCalories());
        food.setProtein(request.getProtein());
        food.setCarbohydrates(request.getCarbohydrates());
        food.setFat(request.getFat());
        food.setFiber(request.getFiber());
        food.setSource(request.getSource());
        
        Food updatedFood = foodRepository.save(food);
        return mapToResponse(updatedFood);
    }
    
    @Transactional
    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new RuntimeException("Yemek bulunamadı");
        }
        foodRepository.deleteById(id);
    }
    
    private FoodResponse mapToResponse(Food food) {
        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .calories(food.getCalories())
                .protein(food.getProtein())
                .carbohydrates(food.getCarbohydrates())
                .fat(food.getFat())
                .fiber(food.getFiber())
                .source(food.getSource())
                .createdAt(food.getCreatedAt())
                .updatedAt(food.getUpdatedAt())
                .build();
    }
}
