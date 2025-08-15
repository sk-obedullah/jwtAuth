package com.ssaa.auth.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssaa.auth.enums.Role;

@RestController
@RequestMapping("/api/public")
public class PublicController {

	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> getPublicInfo() {
		Map<String, Object> info = Map.of("application", "JWT Role-Based Authentication System", "version", "2.0.0",
				"description", "Spring Boot 3.3+ application with JWT authentication and role-based authorization",
				"features",
				List.of("JWT Authentication", "Role-based Authorization", "RESTful APIs", "Spring Security 6.3+"),
				"timestamp", LocalDateTime.now());
		return ResponseEntity.ok(info);
	}

	@GetMapping("/roles")
	public ResponseEntity<Map<String, Object>> getRoleInfo() {
		Map<String, String> roleDescriptions = Map.of(Role.ADMIN.getValue(),
				"Full access to all resources and user management", Role.USER.getValue(),
				"Access to user-specific resources and operations", Role.READONLY.getValue(),
				"View-only access to limited resources");

		Map<String, Object> roleInfo = Map.of("availableRoles", roleDescriptions, "message",
				"Register with appropriate role to access different features", "authEndpoint", "/api/auth/register",
				"loginEndpoint", "/api/auth/login");

		return ResponseEntity.ok(roleInfo);
	}

	@GetMapping("/health")
	public ResponseEntity<Map<String, String>> getHealthStatus() {
		Map<String, String> health = Map.of("status", "UP", "timestamp", LocalDateTime.now().toString());
		return ResponseEntity.ok(health);
	}
}
