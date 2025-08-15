package com.ssaa.auth.dto;

import com.ssaa.auth.enums.Role;

public record JwtResponse(
	    String token,
	    String type,
	    Long id,
	    String username,
	    String email,
	    Role role
	) {
	    public JwtResponse(String token, Long id, String username, String email, Role role) {
	        this(token, "Bearer", id, username, email, role);
	    }
	}