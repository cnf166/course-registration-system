package com.course_registration_system.data.repository;

import com.course_registration_system.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
	Optional<UserEntity> findByEmail(String email); // sau này có thể triển khai OAuth 2.0 log = mail trường
}
