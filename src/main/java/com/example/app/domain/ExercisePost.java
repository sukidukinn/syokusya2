package com.example.app.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePost {

	private Integer id;
	private Integer userId;

	@NotBlank(message = "運動名を入力してください")
	private String exerciseName;

	private String bodyPart; // 腕、足、全身など

	@Min(0)
	private Integer setCount; // セット数

	@Min(0)
	private Integer repetitionCount; // 回数（例：10回）

	@Min(0)
	private Integer durationMinutes; // 分

	private String intensityLevel; // 強度（例：高、中、低）

	@Min(0)
	private Integer calorieBurned;

	private String photoPath;

	private String memo;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime exerciseTime;

	private LocalDateTime created;

	private LocalDateTime updated;

	private Boolean deleted;
}
