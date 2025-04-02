package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Member;
import com.example.app.mapper.UserMapper;
import com.example.app.validation.RegisterGroup;

@Controller
public class MemberController {
	@Autowired
	private UserMapper userMapper;
	
	@GetMapping("/register")
	public String registerGet(Model model) {
		model.addAttribute("member", new Member());
		return "register";
	}

	@PostMapping("/register")
	public String registerPost(
			@Validated(RegisterGroup.class) Member member,
			Errors errors) {
		if (!member.getEmail().equals(member.getEmailConf())) {
			errors.rejectValue("emailConf", "error.email.inequal");
		}
		if (errors.hasErrors()) {
			return "register";
		}
		return "registerDone";
	}
	/*
	@GetMapping("/login")
	public String loginGet(Model model) {
		model.addAttribute("loginMember", new Member());
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email,
			@RequestParam String password,
			HttpSession session,
			Model model) {
		User user = userMapper.findByEmail(email);

		//if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
		if (user != null && (!BCrypt.checkpw(password, user.getPassword())) ) {
			session.setAttribute("loginUser", user);

			// ログイン時刻更新
			user.setLastLogin(LocalDateTime.now());
			userMapper.updateLastLogin(user.getId());

			return "redirect:/mealPosts"; // ログイン後の画面へ
		}

		model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		User user = (User) session.getAttribute("loginUser");

		if (user != null) {
			userMapper.updateLastLogout(user.getId());
			session.invalidate();
		}

		return "redirect:/login";
	}

	/*
	@GetMapping("/login")
	public String loginGet(Model model) {
		model.addAttribute("loginMember", new Member());
		return "login";
	}
	
	@PostMapping("/login")
	public String loginPost(
			@Validated(LoginGroup.class) @ModelAttribute("loginMember") Member loginMember,
			Errors errors) {
		if (errors.hasErrors()) {
			return "login";
		}
		// 未入力でない場合、ID とパスワードをチェック
		if (!loginMember.getEmail().equals("taro@example.com")
				|| !loginMember.getPassword().equals("abcd")) {
			// グローバルエラーの追加
			errors.reject("error.wrong_id_or_password");
			return "login";
		}
		return "loginDone";
	}
	*/
}