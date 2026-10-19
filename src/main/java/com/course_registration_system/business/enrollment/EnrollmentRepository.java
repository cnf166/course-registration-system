package com.course_registration_system.business.enrollment;

import com.course_registration_system.business.enrollment.model.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository {
	Optional<Enrollment> findByStudentAndClass(UUID studentId, String classId);
	List<Enrollment> findByStudentAndSemester(UUID studentId, UUID semesterId);
	Enrollment save(Enrollment enrollment);
	void delete(Enrollment enrollment);
}
