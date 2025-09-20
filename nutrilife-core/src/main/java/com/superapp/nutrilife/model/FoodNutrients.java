package com.superapp.nutrilife.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "food_nutrients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodNutrients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false, foreignKey = @ForeignKey(name = "fk_nutrients_food"))
    private Food food;

    private java.math.BigDecimal energyKcal;

    private java.math.BigDecimal proteinG;

    private java.math.BigDecimal fatG;

    private java.math.BigDecimal carbsG;

    private java.math.BigDecimal fiberG;

    private java.math.BigDecimal sugarG;

    private java.math.BigDecimal sodiumMg;
}
