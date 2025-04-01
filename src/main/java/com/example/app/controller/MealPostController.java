package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.service.MealPostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mealposts")
@RequiredArgsConstructor
public class MealPostController {
	private final MealPostService service;

	@GetMapping
	public String list(Model model) throws Exception {
		model.addAttribute("mealPosts", service.getMealPostList());
		return "mealposts/list";
	}
}

/*
package com.example.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.domain.BookingFormItem;
import com.example.app.domain.MealPostType;

@Controller
@RequestMapping("/mealpost")
public class MealPostController {
	@GetMapping
	public String mealPostGet(Model model) {
		var bookingFormItem = new BookingFormItem();
		// あらかじめ「駐車場を利用しない」を設定
		bookingFormItem.setParking(2);
		model.addAttribute("bookingFormItem", bookingFormItem);
		// 部屋の種類のリスト
		model.addAttribute("mealPostTypeList", getMealPostTypeList());
		return "mealPosting";
	}

	@PostMapping
	public String bookingPost(
			BookingFormItem bookingFormItem,
			Model model) {
		// 施設利用規約への同意がされていない場合、フォームを再表示
		if (!bookingFormItem.getAgreement()) {
			model.addAttribute("mealPostTypeList", getMealPostTypeList());
			return "mealPosting";
		}
		// 完了画面の表示
		return "mealPostingDone";
	}

	// 部屋の種類のリストを返すメソッド
	private List<MealPostType> getMealPostTypeList() {
		List<MealPostType> mealPostTypeList = new ArrayList<>();
		mealPostTypeList.add(new MealPostType(1, "会議室 A", "ホワイトボード有り"));
		mealPostTypeList.add(new MealPostType(2, "会議室 B", "ホワイトボード無し"));
		mealPostTypeList.add(new MealPostType(3, "視聴覚室", "プロジェクター有り"));
		mealPostTypeList.add(new MealPostType(4, "多目的ルーム", "フローリング"));
		return mealPostTypeList;
	}
}
*/