package com.course_registration_system.business.enrollment;

import com.course_registration_system.business.classcourse.ClassCourseRepository;
import com.course_registration_system.business.enrollment.model.Enrollment;
import com.course_registration_system.business.enrollmentperiod.EnrollmentPeriodRepository;

import java.time.Clock;
import java.util.List;

// Depends on other modules' ports, not their services, to avoid circular service dependencies.
public class EnrollmentService {
	private final EnrollmentRepository enrollmentRepository;
	private final ClassCourseRepository classCourseRepository;
	private final EnrollmentPeriodRepository enrollmentPeriodRepository;
	private final Clock clock;

	public EnrollmentService(EnrollmentRepository enrollmentRepository,
							 ClassCourseRepository classCourseRepository,
							 EnrollmentPeriodRepository enrollmentPeriodRepository,
							 Clock clock) {
		this.enrollmentRepository = enrollmentRepository;
		this.classCourseRepository = classCourseRepository;
		this.enrollmentPeriodRepository = enrollmentPeriodRepository;
		this.clock = clock;
	}

	public Enrollment enroll(String studentId, String classId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void cancel(String studentId, String classId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public List<Enrollment> getMyClasses(String studentId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
