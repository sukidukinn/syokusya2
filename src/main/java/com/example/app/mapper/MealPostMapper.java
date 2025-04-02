package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.MealPost;

public interface MealPostMapper {
	List<MealPost> selectAll() throws Exception;

	MealPost selectById(Integer id) throws Exception;

	void insert(MealPost mealPost) throws Exception;

	void update(MealPost mealPost) throws Exception;

	void delete(Integer id) throws Exception;

	// ★ 追加: ユーザーIDによる投稿取得
	List<MealPost> findByUserId(Integer userId) throws Exception;

	// ★ 追加: 論理削除
	void softDeleteById(Integer id) throws Exception;
}
