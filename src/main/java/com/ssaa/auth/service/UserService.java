package com.ssaa.auth.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ssaa.auth.dto.RegisterRequest;
import com.ssaa.auth.dto.UpdateUserRequest;
import com.ssaa.auth.dto.UserResponse;
import com.ssaa.auth.entity.User;
import com.ssaa.auth.enums.Role;
import com.ssaa.auth.exception.ResourceNotFoundException;
import com.ssaa.auth.exception.UserAlreadyExistsException;
import com.ssaa.auth.repository.UserRepository;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public UserResponse createUser(RegisterRequest request) {
        validateUserCreation(request);
        
        User user = new User(
            request.username(),
            request.email(),
            passwordEncoder.encode(request.password()),
            request.role()
        );
        
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponse::new);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return new UserResponse(user);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return new UserResponse(user);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(UserResponse::new)
                .toList();
    }
    
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findUserById(id);
        
        validateUserUpdate(user, request);
        updateUserFields(user, request);
        
        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }
    
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }
    
    public UserResponse toggleUserStatus(Long id) {
        User user = findUserById(id);
        user.setEnabled(!user.getEnabled());
        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }
    
    @Transactional(readOnly = true)
    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getActiveUsers() {
        return userRepository.findAllActiveUsers()
                .stream()
                .map(UserResponse::new)
                .toList();
    }
    
    // Private helper methods
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    private void validateUserCreation(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username is already taken: " + request.username());
        }
        
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email is already in use: " + request.email());
        }
    }
    
    private void validateUserUpdate(User user, UpdateUserRequest request) {
        if (StringUtils.hasText(request.username()) && 
            !user.getUsername().equals(request.username()) && 
            userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username is already taken: " + request.username());
        }
        
        if (StringUtils.hasText(request.email()) && 
            !user.getEmail().equals(request.email()) && 
            userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email is already in use: " + request.email());
        }
    }
    
    private void updateUserFields(User user, UpdateUserRequest request) {
        if (StringUtils.hasText(request.username())) {
            user.setUsername(request.username());
        }
        if (StringUtils.hasText(request.email())) {
            user.setEmail(request.email());
        }
        if (StringUtils.hasText(request.password())) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.role() != null) {
            user.setRole(request.role());
        }
        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }
    }
}
