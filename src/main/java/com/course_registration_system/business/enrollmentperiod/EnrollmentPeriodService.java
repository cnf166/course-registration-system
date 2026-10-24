package com.course_registration_system.business.enrollmentperiod;

import com.course_registration_system.business.enrollmentperiod.model.EnrollmentPeriod;

import java.time.Clock;

public class EnrollmentPeriodService {
	private final EnrollmentPeriodRepository enrollmentPeriodRepository;
	private final Clock clock;

	public EnrollmentPeriodService(EnrollmentPeriodRepository enrollmentPeriodRepository, Clock clock) {
		this.enrollmentPeriodRepository = enrollmentPeriodRepository;
		this.clock = clock;
	}

	public EnrollmentPeriod getCurrent() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public EnrollmentPeriod update(EnrollmentPeriod period) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
