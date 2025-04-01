package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.MealPost;
import com.example.app.domain.MealPostType;
import com.example.app.mapper.MealPostMapper;
import com.example.app.mapper.MealPostTypeMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MealPostServiceImpl implements MealPostService {
	private final MealPostMapper memberMapper;
	private final MealPostTypeMapper memberTypeMapper;

	@Override
	public List<MealPost> getMealPostList() throws Exception {
		return memberMapper.selectAll();
	}

	@Override
	public MealPost getMealPostById(Integer id) throws Exception {
		return memberMapper.selectById(id);
	}

	@Override
	public void addMealPost(MealPost member) throws Exception {
		memberMapper.insert(member);
	}

	@Override
	public void editMealPost(MealPost member) throws Exception {
		memberMapper.update(member);
	}

	@Override
	public void deleteMealPost(Integer id) throws Exception {
		memberMapper.delete(id);
	}

	@Override
	public List<MealPostType> getTypeList() throws Exception {
		return memberTypeMapper.selectAll();
	}
}
