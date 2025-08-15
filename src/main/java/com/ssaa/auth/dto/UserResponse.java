package com.ssaa.auth.dto;

import java.time.LocalDateTime;

import com.ssaa.auth.entity.User;
import com.ssaa.auth.enums.Role;

public record UserResponse(
	    Long id,
	    String username,
	    String email,
	    Role role,
	    Boolean enabled,
	    LocalDateTime createdAt,
	    LocalDateTime updatedAt
	) {
	    public UserResponse(User user) {
	        this(
	            user.getId(),
	            user.getUsername(),
	            user.getEmail(),
	            user.getRole(),
	            user.getEnabled(),
	            user.getCreatedAt(),
	            user.getUpdatedAt()
	        );
	    }
	}