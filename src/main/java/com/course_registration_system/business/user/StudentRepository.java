package com.course_registration_system.business.user;

import com.course_registration_system.business.user.model.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {
	Optional<Student> findById(UUID id);
	Optional<Student> findByStudentId(String studentId);
	Student save(Student student);
}
