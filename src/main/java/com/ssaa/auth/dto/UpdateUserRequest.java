package com.ssaa.auth.dto;

import com.ssaa.auth.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
	    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	    String username,
	    
	    @Email(message = "Email should be valid")
	    String email,
	    
	    @Size(min = 6, message = "Password must be at least 6 characters")
	    String password,
	    
	    Role role,
	    
	    Boolean enabled
	) {}