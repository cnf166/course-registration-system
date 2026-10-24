package com.course_registration_system.business.enrollmentperiod;

import com.course_registration_system.business.enrollmentperiod.model.EnrollmentPeriod;

import java.util.Optional;

public interface EnrollmentPeriodRepository {
	// Empty until an admin sets the period for the first time.
	Optional<EnrollmentPeriod> findCurrent();

	EnrollmentPeriod save(EnrollmentPeriod period);
}
