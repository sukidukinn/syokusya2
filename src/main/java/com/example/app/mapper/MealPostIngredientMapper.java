package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.MealPostIngredient;

@Mapper
public interface MealPostIngredientMapper {
    List<MealPostIngredient> selectByMealPostId(@Param("mealPostId") int mealPostId);
}