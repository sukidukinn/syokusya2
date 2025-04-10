package com.example.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.ExercisePost;
import com.example.app.domain.MealPost;
import com.example.app.domain.User;
import com.example.app.mapper.ExercisePostMapper;
import com.example.app.mapper.MealPostMapper;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/events")
public class EventController {

	@Autowired
	private MealPostMapper mealPostMapper;

	@Autowired
	private ExercisePostMapper exercisePostMapper;

	@GetMapping("/all")
	public List<Map<String, Object>> getAllEvents(HttpSession session) throws Exception {
	    User user = (User) session.getAttribute("loginUser");
	    List<Map<String, Object>> events = new ArrayList<>();

	    // 食事投稿からイベント生成
	    List<MealPost> meals = mealPostMapper.findByUserId(user.getId());
	    for (MealPost meal : meals) {
	        Map<String, Object> event = new HashMap<>();
	        event.put("title", "🍽️ " + meal.getMealName());
	        event.put("start", meal.getMealTime().toString());
	        event.put("url", "/mealPosts/edit/" + meal.getId());
	        if (meal.getPhotoPath() != null) {
	            event.put("photoPath", meal.getPhotoPath());
	        }
	        events.add(event);
	    }

	    // エクササイズ投稿からイベント生成
	    List<ExercisePost> exercises = exercisePostMapper.findByUserId(user.getId());
	    for (ExercisePost exercise : exercises) {
	        Map<String, Object> event = new HashMap<>();
	        event.put("title", "💪 " + exercise.getExerciseName());
	        event.put("start", exercise.getExerciseTime().toString());
	        event.put("url", "/exercisePosts/edit/" + exercise.getId());
	        if (exercise.getPhotoPath() != null) {
	            event.put("photoPath", exercise.getPhotoPath());
	        }
	        events.add(event);
	    }
	    
	    return events;
	}
}
