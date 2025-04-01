package com.example.app.service;

import java.util.List;

import com.example.app.domain.MealPost;
import com.example.app.domain.MealPostType;

public interface MealPostService {
	List<MealPost> getMealPostList() throws Exception;

	MealPost getMealPostById(Integer id) throws Exception;

	void addMealPost(MealPost member) throws Exception;

	void editMealPost(MealPost member) throws Exception;

	void deleteMealPost(Integer id) throws Exception;

	List<MealPostType> getTypeList() throws Exception;
}