package com.superapp.nutrilife.dto;

import com.superapp.nutrilife.model.enums.MealType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MealRequest {
    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long userId;
    
    @NotNull(message = "Öğün tipi boş olamaz")
    private MealType mealType;
    
    @NotNull(message = "Öğün tarihi boş olamaz")
    private LocalDate mealDate;
    
    private List<MealItemRequest> mealItems;
}
