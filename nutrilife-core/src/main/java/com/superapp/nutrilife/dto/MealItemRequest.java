package com.superapp.nutrilife.dto;

import com.superapp.nutrilife.model.enums.ServingUnit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MealItemRequest {
    @NotNull(message = "Yemek ID boş olamaz")
    private Long foodId;
    
    @NotNull(message = "Miktar boş olamaz")
    @Positive(message = "Miktar pozitif olmalıdır")
    private Double quantity;
    
    @NotNull(message = "Porsiyon birimi boş olamaz")
    private ServingUnit servingUnit;
}
