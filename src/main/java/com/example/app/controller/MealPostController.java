package com.example.app.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.MealPost;
import com.example.app.domain.MealPostIngredient;
import com.example.app.domain.NutritionFood;
import com.example.app.domain.User;
import com.example.app.mapper.MealPostIngredientMapper;
import com.example.app.mapper.MealPostMapper;
import com.example.app.mapper.NutritionFoodMapper;
import com.example.app.service.MealPostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MealPostController {
	@Value("${upload.path}")
	private String uploadPath;

	private final MealPostService mealPostService; // ← 名前をmealPostServiceに統一

	@Autowired
	private MealPostMapper mealPostMapper;

	@Autowired
	private MealPostIngredientMapper mealPostIngredientMapper;

	@Autowired
	private NutritionFoodMapper nutritionFoodMapper;
	
	
	@PostMapping("/mealPosts/save")
	public String saveMealPost(@ModelAttribute MealPost mealPost,
			@RequestParam("photoFile") MultipartFile photoFile,
			HttpSession session) throws Exception {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// ファイルアップロード処理
		if (!photoFile.isEmpty()) {
			String uploadDir = uploadPath;
			String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
			File destFile = new File(uploadDir, filename);
			photoFile.transferTo(destFile);

			// photoPathにWebアクセス用のパスを保存
			mealPost.setPhotoPath("uploads/" + filename);
		} else {
			// アップロードがなければ、既存の画像を維持
			if (mealPost.getId() != null) {
				MealPost existing = mealPostMapper.selectById(mealPost.getId());
				if (existing != null && existing.getPhotoPath() != null) {
					mealPost.setPhotoPath(existing.getPhotoPath());
				}
			}
		}

		// ユーザーIDを設定
		mealPost.setUserId(loginUser.getId());

		if (mealPost.getId() == null) {
			mealPostService.addMealPost(mealPost);
		} else {
			mealPostService.editMealPost(mealPost);
		}
		return "redirect:/mealposts";
	}

	@GetMapping("/mealPosts/add")
	public String addMealPostForm(Model model) {
		MealPost emptyPost = new MealPost();
		emptyPost.setMealTime(LocalDateTime.now());
		model.addAttribute("mealPost", emptyPost);
		model.addAttribute("ingredients", List.of()); // 新規なので空

	    List<NutritionFood> nutritionFoods = nutritionFoodMapper.selectAll();
	    model.addAttribute("nutritionFoods", nutritionFoods);
	    
		return "mealposts/detail";
	}

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

	// 食事投稿削除
	@GetMapping("/mealPosts/delete/{id}")
	public String deleteMealPost(@PathVariable Integer id) throws Exception {
		mealPostMapper.softDeleteById(id);
		return "redirect:/mealposts";
	}

	// 画像のみ削除
	@GetMapping("/mealPosts/{id}/clearImage")
	public String clearMealPostImage(@PathVariable Integer id) throws Exception {
		MealPost post = mealPostMapper.selectById(id);
		if (post != null && post.getPhotoPath() != null) {
			// 実ファイルも削除
			File file = new File(uploadPath + "/" + post.getPhotoPath().replace("uploads/", ""));
			if (file.exists())
				file.delete();

			// DB上のphotoPathをnullに更新
			post.setPhotoPath(null);
			mealPostMapper.update(post);
		}
		return "redirect:/mealPosts/edit/" + id;
	}
	
	@GetMapping("/mealPosts/edit/{id}")
	public String editMealPost(@PathVariable Integer id, Model model) throws Exception {
		MealPost mealPost = mealPostMapper.selectById(id);
		if (mealPost == null) {
			return "redirect:/mealPosts";
		}

	    List<MealPostIngredient> ingredients = mealPostIngredientMapper.selectByMealPostId(id);
	    List<NutritionFood> nutritionFoods = nutritionFoodMapper.selectAll();

	    System.out.println("▼ ingredients:");
	    for (MealPostIngredient ing : ingredients) {
	        System.out.println("  ID=" + ing.getId() +
		            ", postId=" + ing.getMealPostId() +
	            ", foodId=" + ing.getNutritionFoodId() +
	            ", grams=" + ing.getAmountGrams());
	    }
	    
		model.addAttribute("mealPost", mealPost);
	    model.addAttribute("ingredients", ingredients);
	    model.addAttribute("nutritionFoods", nutritionFoods);
	    
	    
		return "mealposts/detail";
	}
}
