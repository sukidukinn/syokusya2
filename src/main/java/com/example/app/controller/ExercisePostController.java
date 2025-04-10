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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.ExercisePost;
import com.example.app.domain.User;
import com.example.app.mapper.ExercisePostMapper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/exercisePosts") // ← これがある場合
public class ExercisePostController {
	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	private ExercisePostMapper exercisePostMapper;

	@GetMapping
	public String list(Model model, HttpSession session) {
		User user = (User) session.getAttribute("loginUser");
		List<ExercisePost> exercisePosts = exercisePostMapper.findByUserId(user.getId());
		model.addAttribute("exercisePosts", exercisePosts);
		return "exerciseposts/detail"; // ※統合表示なら別ページでもOK
	}

	@GetMapping("/add")
	public String add(Model model) {
		ExercisePost post = new ExercisePost();
		post.setExerciseName("ランニング");
		post.setBodyPart("足");
		post.setDurationMinutes(30);
		post.setSetCount(1);
		post.setRepetitionCount(1);
		post.setCalorieBurned(200); // ✅ ← 必須！
		post.setIntensityLevel("中程度");
		post.setExerciseTime(LocalDateTime.now());

		model.addAttribute("exercisePost", post);
		return "exerciseposts/detail";
	}

	@PostMapping("/save")
	public String saveExercisePost(
			@ModelAttribute ExercisePost exercisePost,
			@RequestParam("photoFile") MultipartFile photoFile, // ← これ必須
			@RequestParam("action") String action,
			HttpSession session,
			Model model) throws Exception {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		if (!photoFile.isEmpty()) {
			String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
			File destFile = new File(uploadPath, filename);
			photoFile.transferTo(destFile);
			exercisePost.setPhotoPath("uploads/" + filename);
		} else if (exercisePost.getId() != null) {
			ExercisePost existing = exercisePostMapper.selectById(exercisePost.getId());
			if (existing != null && existing.getPhotoPath() != null) {
				exercisePost.setPhotoPath(existing.getPhotoPath());
			}
		}

		exercisePost.setUserId(loginUser.getId());
		if (exercisePost.getDeleted() == null) {
			exercisePost.setDeleted(false);
		}

		// 挿入 or 更新
		if (exercisePost.getId() == null) {
			exercisePostMapper.insert(exercisePost);
		} else {
			exercisePostMapper.update(exercisePost);
		}

		if ("saveOnly".equals(action)) {
			ExercisePost updated = exercisePostMapper.selectById(exercisePost.getId());
			model.addAttribute("exercisePost", updated);
			model.addAttribute("pageMessage", "保存しました");
			return "redirect:/exercisePosts/edit/" + exercisePost.getId();
		}

		return "redirect:/mealposts";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int id, Model model) {
		ExercisePost post = exercisePostMapper.selectById(id);
		model.addAttribute("exercisePost", post);
		return "exerciseposts/detail";
	}

	@GetMapping("/delete/{id}")
	public String deleteExercisePost(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		ExercisePost post = exercisePostMapper.selectById(id);
		System.out.println("🔍 削除対象のID: " + id);

		if (post != null) {
			System.out.println("📄 取得した投稿: " + post);

			post.setDeleted(true);
			exercisePostMapper.update(post); // 論理削除

			System.out.println("📝 論理削除フラグをtrueに設定しました");
			redirectAttributes.addFlashAttribute("pageMessage", "エクササイズ投稿を削除しました");
		} else {
			System.out.println("⚠️ 指定IDの投稿が見つかりません");
		}

		return "redirect:/mealposts";
	}
}
