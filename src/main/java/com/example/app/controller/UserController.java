package com.example.app.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.User;
import com.example.app.mapper.MealPostMapper;
import com.example.app.mapper.UserMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserMapper userMapper;
	
	@Autowired
	private MealPostMapper mealPostMapper;

	@GetMapping("/mealPosts")
	public String mealPostList(Model model, HttpSession session) throws Exception {
		// ログインチェック等…
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("mealPosts", mealPostMapper.findByUserId(loginUser.getId()));
		return "mealposts/list"; // HTMLファイル名に合わせて
	}

	// 会員一覧の表示
	@GetMapping("/users")
	public String list(Model model) throws Exception {
		model.addAttribute("users", userMapper.selectUsers());
		return "users";
	}

	// 会員追加
	@PostMapping("/add")
	public String add(@Valid User user, Errors errors) throws Exception {
		if (errors.hasErrors()) {
			return "addUser";
		}

		userMapper.addUser(user);
		return "redirect:/";
	}

	@GetMapping("/profile")
	public String showProfile(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// ユーザー情報を取得し直して表示用に設定
		User user = userMapper.selectById(loginUser.getId());
		model.addAttribute("user", user);

		// pageMessageがFlashAttributeで来ていたら表示用に渡す
		if (!model.containsAttribute("pageMessage")) {
			model.addAttribute("pageMessage", "");
		}

		return "profile";
	}

	@PostMapping("/profile/update")
	public String updateProfile(
			@Valid @ModelAttribute("user") User user,
			BindingResult bindingResult,
			HttpSession session,
			@RequestParam("action") String action,
			RedirectAttributes redirectAttrs,
			Model model) {

		// セッションのログインユーザー取得
		User loginUser = (User) session.getAttribute("loginUser");
		user.setId(loginUser.getId());

		// バリデーションエラーがある場合は元画面に戻す
		if (bindingResult.hasErrors()) {
			model.addAttribute("pageMessage", "入力内容に誤りがあります。");
			return "profile";
		}

		// パスワードが空欄なら既存のものを保持
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			user.setPassword(loginUser.getPassword());
		} else {
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		}

		// BMI/BMR等の自動計算があればここに追加
		userMapper.updateUser(user);
		session.setAttribute("loginUser", user);

		if ("saveAndBack".equals(action)) {
			redirectAttrs.addFlashAttribute("pageMessage", "プロフィールを更新しました");
			return "redirect:/mealPosts";
		} else {
			model.addAttribute("pageMessage", "プロフィールを更新しました（画面はそのまま）");
			return "profile";
		}
	}

}