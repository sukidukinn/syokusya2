package com.example.app.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {
	private Integer id;
	@NotBlank(message = "名前を入力してください")
	@Size(max = 10, message = "10 字以内で入力してください")
	private String name;
	@Min(value = 0, message = "0 以上の整数を入力してください")
	private Integer age;
	private String address;
	private Integer typeId;
	private LocalDateTime created;
}