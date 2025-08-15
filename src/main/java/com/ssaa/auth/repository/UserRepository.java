package com.ssaa.auth.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssaa.auth.entity.User;
import com.ssaa.auth.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	List<User> findByRole(Role role);

	Page<User> findByRole(Role role, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.enabled = :enabled")
	List<User> findByEnabled(@Param("enabled") boolean enabled);

	@Query("SELECT u FROM User u WHERE u.enabled = true")
	List<User> findAllActiveUsers();

	@Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
	long countByRole(@Param("role") Role role);

	@Query("SELECT u FROM User u WHERE u.createdAt >= :from AND u.createdAt <= :to")
	List<User> findUsersCreatedBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

	@Modifying
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
	void updateUserStatus(@Param("id") Long id, @Param("enabled") boolean enabled);
}