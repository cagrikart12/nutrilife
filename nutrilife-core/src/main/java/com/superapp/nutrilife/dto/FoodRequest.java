package com.superapp.nutrilife.dto;

import com.superapp.nutrilife.model.enums.FoodSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FoodRequest {
    @NotBlank(message = "Yemek adı boş olamaz")
    private String name;
    
    private String description;
    
    @NotNull(message = "Kalori değeri boş olamaz")
    @Positive(message = "Kalori değeri pozitif olmalıdır")
    private Double calories;
    
    @Positive(message = "Protein değeri pozitif olmalıdır")
    private Double protein;
    
    @Positive(message = "Karbonhidrat değeri pozitif olmalıdır")
    private Double carbohydrates;
    
    @Positive(message = "Yağ değeri pozitif olmalıdır")
    private Double fat;
    
    @Positive(message = "Fiber değeri pozitif olmalıdır")
    private Double fiber;
    
    private FoodSource source = FoodSource.USER;
}
