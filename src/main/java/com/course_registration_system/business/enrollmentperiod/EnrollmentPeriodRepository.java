package com.course_registration_system.business.semester;

import com.course_registration_system.business.semester.model.EnrollmentPeriod;

import java.time.Instant;
import java.util.List;

public interface EnrollmentPeriodRepository {
	List<EnrollmentPeriod> findActiveAt(Instant instant);
	EnrollmentPeriod save(EnrollmentPeriod period);
}
