package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MealPost {
    private Integer id;
    private Integer userId;

    @NotBlank(message = "食事名を入力してください")
    private String mealName;

    @NotNull(message = "食事時間を入力してください")
    private LocalDateTime mealTime;

    private String photoPath;
    private String memo;

    private BigDecimal totalCalorie;
    private BigDecimal proteinG;
    private BigDecimal fatG;
    private BigDecimal carbohydrateG;
    private BigDecimal fiberG;
    private BigDecimal saltG;
    private BigDecimal cholesterolMg;

    private LocalDateTime created;
    private Boolean deleted;
}