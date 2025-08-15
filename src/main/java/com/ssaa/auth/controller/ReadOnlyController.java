package com.ssaa.auth.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssaa.auth.dto.UserResponse;
import com.ssaa.auth.entity.User;
import com.ssaa.auth.service.UserService;

@RestController
@RequestMapping("/api/readonly")
@PreAuthorize("hasRole('READONLY') or hasRole('USER') or hasRole('ADMIN')")
public class ReadOnlyController {

	private final UserService userService;

	public ReadOnlyController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/profile")
	public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		UserResponse userResponse = userService.getUserById(user.getId());
		return ResponseEntity.ok(userResponse);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<Map<String, Object>> getReadOnlyDashboard(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		Map<String, Object> dashboard = Map.of("welcome", "Welcome, " + user.getUsername() + "!", "role",
				user.getRole(), "message", "You have read-only access to view resources.", "permissions",
				List.of("read"), "restrictions", "No create, update, or delete operations allowed", "lastAccess",
				LocalDateTime.now());

		return ResponseEntity.ok(dashboard);
	}

	@GetMapping("/public-data")
	public ResponseEntity<Map<String, Object>> getPublicData() {
		Map<String, Object> data = Map.of("message", "This is public data accessible to all authenticated users",
				"data", Map.of("items", List.of("Item1", "Item2", "Item3"), "total", 3, "category", "public"),
				"timestamp", LocalDateTime.now());
		return ResponseEntity.ok(data);
	}

	@GetMapping("/statistics")
	public ResponseEntity<Map<String, Object>> getPublicStatistics() {
		Map<String, Object> stats = Map.of("totalUsers", userService.getAllUsers().size(), "activeUsers",
				userService.getActiveUsers().size(), "systemStatus", "operational", "uptime", "99.9%");
		return ResponseEntity.ok(stats);
	}
}