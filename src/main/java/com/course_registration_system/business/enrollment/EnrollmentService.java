package com.course_registration_system.business.enrollment;

import com.course_registration_system.business.courseclass.CourseClassRepository;
import com.course_registration_system.business.courseclass.model.CourseClass;
import com.course_registration_system.business.enrollment.model.Enrollment;
import com.course_registration_system.business.semester.EnrollmentPeriodRepository;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

// Depends on other modules' ports, not their services, to avoid circular service dependencies.
public class EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	private final CourseClassRepository courseClassRepository;
	private final EnrollmentPeriodRepository enrollmentPeriodRepository;
	private final Clock clock;

	public EnrollmentService(EnrollmentRepository enrollmentRepository,
							 CourseClassRepository courseClassRepository,
							 EnrollmentPeriodRepository enrollmentPeriodRepository,
							 Clock clock) {
		this.enrollmentRepository = enrollmentRepository;
		this.courseClassRepository = courseClassRepository;
		this.enrollmentPeriodRepository = enrollmentPeriodRepository;
		this.clock = clock;
	}

	public Enrollment enroll(UUID studentId, String classId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void cancel(UUID studentId, String classId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public List<Enrollment> getMyClasses(UUID studentId, UUID semesterId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	// The timetable is the schedule of every class the student is enrolled in this semester.
	public List<CourseClass> getMySchedule(UUID studentId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
