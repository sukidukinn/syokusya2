package com.example.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

	@GetMapping("/all")
	public List<Map<String, String>> getAllEvents() {
		List<Map<String, String>> events = new ArrayList<>();

		Map<String, String> e1 = new HashMap<>();
		e1.put("title", "食事記録：朝食");
		e1.put("start", "2025-04-12");
		events.add(e1);

		Map<String, String> e2 = new HashMap<>();
		e2.put("title", "カロリー超過！");
		e2.put("start", "2025-04-13");
		e2.put("color", "red");
		events.add(e2);

		return events;
	}
}