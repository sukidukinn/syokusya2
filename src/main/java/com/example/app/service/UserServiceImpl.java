package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public User findByEmail(String email) {
		return userMapper.findByEmail(email);
	}

	@Override
	public void updateLastLogin(int userId) {
		userMapper.updateLastLogin(userId);
	}

	@Override
	public void updateLastLogout(int userId) {
		userMapper.updateLastLogout(userId);
	}
}