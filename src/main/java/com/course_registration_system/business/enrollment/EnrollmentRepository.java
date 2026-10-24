package com.course_registration_system.business.enrollment;

import com.course_registration_system.business.enrollment.model.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
	List<Enrollment> findByStudent(String studentId);

	Optional<Enrollment> findByStudentAndClass(String studentId, String classId);

	int countByClass(String classId);

	Enrollment save(Enrollment enrollment);

	void delete(Enrollment enrollment);
}
