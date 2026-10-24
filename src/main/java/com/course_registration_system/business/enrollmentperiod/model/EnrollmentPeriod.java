package com.course_registration_system.business.enrollmentperiod.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

// The single registration period shared by all students.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentPeriod {
	private Instant startAt; // inclusive
	private Instant endAt; // exclusive
	private Instant updatedAt;
}
