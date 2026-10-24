package com.course_registration_system.business.semester;

import com.course_registration_system.business.semester.model.EnrollmentPeriod;
import com.course_registration_system.business.semester.model.Semester;

import java.time.Clock;
import java.util.List;

public class SemesterService {

	private final SemesterRepository semesterRepository;
	private final EnrollmentPeriodRepository enrollmentPeriodRepository;
	private final Clock clock;

	public SemesterService(SemesterRepository semesterRepository,
						   EnrollmentPeriodRepository enrollmentPeriodRepository,
						   Clock clock) {
		this.semesterRepository = semesterRepository;
		this.enrollmentPeriodRepository = enrollmentPeriodRepository;
		this.clock = clock;
	}

	public Semester getCurrent() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public List<EnrollmentPeriod> getActivePeriods() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public EnrollmentPeriod createPeriod(EnrollmentPeriod period) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
