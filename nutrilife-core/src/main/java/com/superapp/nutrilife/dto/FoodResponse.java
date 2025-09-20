package com.superapp.nutrilife.dto;

import com.superapp.nutrilife.model.enums.FoodSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private String description;
    private Double calories;
    private Double protein;
    private Double carbohydrates;
    private Double fat;
    private Double fiber;
    private FoodSource source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
