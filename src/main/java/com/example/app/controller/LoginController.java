package com.example.app.controller;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.LoginForm;
import com.example.app.domain.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {

	private final UserService userService;

	public LoginController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("loginMember", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("loginMember") @Valid LoginForm form,
			BindingResult result,
			HttpSession session) {

		if (result.hasErrors()) {
			return "login";
		}

		User loginUser = userService.findByEmail(form.getEmail());
		//!BCrypt.checkpw(form.getPassword(), loginUser.getPassword()
		if (loginUser == null || !BCrypt.checkpw(form.getPassword(), loginUser.getPassword() )) {
			result.reject("login.failed", "メールアドレスまたはパスワードが正しくありません");
			return "login";
		}

		session.setAttribute("loginUser", loginUser);
		return "redirect:/mealposts";
	}
}