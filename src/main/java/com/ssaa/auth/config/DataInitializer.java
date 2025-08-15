package com.ssaa.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ssaa.auth.entity.User;
import com.ssaa.auth.enums.Role;
import com.ssaa.auth.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        initializeDefaultUsers();
    }
    
    private void initializeDefaultUsers() {
        createDefaultUser("admin", "admin@ssaa.com", "admin123", Role.ADMIN);
        createDefaultUser("user", "user@ssaa.com", "user123", Role.USER);
        createDefaultUser("readonly", "readonly@ssaa.com", "readonly123", Role.READONLY);
        
        logger.info("Database initialized with {} users", userRepository.count());
    }
    
    private void createDefaultUser(String username, String email, String password, Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);
            
            logger.info("Created default {} user: username={}, password={}", 
                       role.getValue().toLowerCase(), username, password);
        } else {
            logger.info("User {} already exists, skipping creation", username);
        }
    }
}
