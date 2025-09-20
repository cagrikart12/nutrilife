package com.superapp.nutrilife.model;

import com.superapp.nutrilife.model.enums.ServingUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "meal_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;
    
    @NotNull(message = "Miktar boş olamaz")
    @Positive(message = "Miktar pozitif olmalıdır")
    @Column(nullable = false)
    private Double quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServingUnit servingUnit;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}