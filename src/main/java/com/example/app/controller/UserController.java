package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	private final UserMapper mapper;

	// 会員一覧の表示
	@GetMapping("/users")
	public String list(Model model) throws Exception {
		model.addAttribute("users", mapper.selectUsers());
		return "users";
	}

	// 会員追加フォームの表示
	@GetMapping("/add")
	public String add(Model model) throws Exception {
		User user = new User();
		user.setAge(20);
		user.setTypeId(1);
		model.addAttribute("user", user);
		return "addUser";
	}

	// 会員追加
	@PostMapping("/add")
	public String add(@Valid User user, Errors errors) throws Exception {
		if (errors.hasErrors()) {
			return "addUser";
		}

		mapper.addUser(user);
		return "redirect:/";
	}
}