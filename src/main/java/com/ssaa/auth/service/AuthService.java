package com.ssaa.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssaa.auth.dto.JwtResponse;
import com.ssaa.auth.dto.LoginRequest;
import com.ssaa.auth.dto.RegisterRequest;
import com.ssaa.auth.dto.UserResponse;
import com.ssaa.auth.entity.User;
import com.ssaa.auth.security.jwt.JwtUtils;

@Service
@Transactional
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final UserService userService;

	public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
	}

	public JwtResponse authenticateUser(LoginRequest request) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken((User) authentication.getPrincipal());

		User userPrincipal = (User) authentication.getPrincipal();

		return new JwtResponse(jwt, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getEmail(),
				userPrincipal.getRole());
	}

	public UserResponse registerUser(RegisterRequest request) {
		return userService.createUser(request);
	}

	public void logout() {
		SecurityContextHolder.clearContext();
	}
}
