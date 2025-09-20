package com.superapp.nutrilife.model;

import com.superapp.nutrilife.model.enums.FoodSource;
import com.superapp.nutrilife.model.enums.ServingUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "foods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Yemek adı boş olamaz")
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Kalori değeri boş olamaz")
    @Positive(message = "Kalori değeri pozitif olmalıdır")
    @Column(nullable = false)
    private Double calories;
    
    @Positive(message = "Protein değeri pozitif olmalıdır")
    private Double protein;
    
    @Positive(message = "Karbonhidrat değeri pozitif olmalıdır")
    private Double carbohydrates;
    
    @Positive(message = "Yağ değeri pozitif olmalıdır")
    private Double fat;
    
    @Positive(message = "Fiber değeri pozitif olmalıdır")
    private Double fiber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodSource source = FoodSource.USER;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealItem> mealItems;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}