package com.course_registration_system.data.repository;

import com.course_registration_system.data.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaStudentRepository extends JpaRepository<StudentEntity, UUID> {
	Optional<StudentEntity> findByStudentId(String studentId);
}
