package com.course_registration_system.data.repository;

import com.course_registration_system.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email); // sau này có thể triển khai OAuth 2.0 log = mail trường
}
