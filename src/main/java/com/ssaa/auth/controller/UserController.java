package com.ssaa.auth.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssaa.auth.dto.UserResponse;
import com.ssaa.auth.entity.User;
import com.ssaa.auth.service.UserService;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponse userResponse = userService.getUserById(user.getId());
        return ResponseEntity.ok(userResponse);
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getUserDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> dashboard = Map.of(
            "welcome", "Welcome, " + user.getUsername() + "!",
            "role", user.getRole(),
            "message", "You have USER or ADMIN privileges.",
            "lastLogin", LocalDateTime.now(),
            "permissions", List.of("create", "read", "update", "delete")
        );
        
        return ResponseEntity.ok(dashboard);
    }
    
    @PostMapping("/action")
    public ResponseEntity<Map<String, String>> performUserAction() {
        Map<String, String> response = Map.of(
            "message", "User action performed successfully",
            "action", "create/update/delete user resources",
            "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}
