package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MealPostIngredient {
	private Integer id;
	private Integer mealPostId;
	private Integer nutritionFoodId;
	private String nutritionFoodName; // JOINで取得
	private BigDecimal amountG;
	private BigDecimal proteinG;
	private BigDecimal fatG;
	private BigDecimal carbohydrateG;
	private Boolean deleted;
}