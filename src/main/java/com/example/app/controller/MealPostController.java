package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.app.domain.MealPost;
import com.example.app.domain.MealPostIngredient;
import com.example.app.domain.User;
import com.example.app.mapper.MealPostIngredientMapper;
import com.example.app.mapper.MealPostMapper;
import com.example.app.service.MealPostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MealPostController {

	private final MealPostService mealPostService; // ← 名前をmealPostServiceに統一

	@Autowired
	private MealPostMapper mealPostMapper;

	@Autowired
	private MealPostIngredientMapper mealPostIngredientMapper;

	@GetMapping("/mealposts")
	public String list(Model model, HttpSession session) throws Exception {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		List<MealPost> posts;
		if (loginUser.getTypeId() == 3) { //運営モード
			posts = mealPostService.getMealPostList();
		} else {
			posts = mealPostService.getMealPostsByUserId(loginUser.getId());
		}

		model.addAttribute("mealPosts", posts);
		model.addAttribute("loginUser", loginUser);
		return "mealposts/list";
	}

	@GetMapping("/mealPosts/delete/{id}")
	public String delete(@PathVariable Integer id) throws Exception {
		mealPostMapper.softDeleteById(id);
		return "redirect:/mealPosts";
	}

	@GetMapping("/mealPosts/edit/{id}")
	public String editMealPost(@PathVariable Integer id, Model model) throws Exception {
		MealPost mealPost = mealPostMapper.selectById(id);
		if (mealPost == null) {
			return "redirect:/mealPosts";
		}
		List<MealPostIngredient> ingredients = mealPostIngredientMapper.selectByMealPostId(id);
		model.addAttribute("mealPost", mealPost);
		model.addAttribute("ingredients", ingredients);
		return "mealposts/detail";
	}
}
