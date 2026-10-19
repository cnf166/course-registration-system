package com.course_registration_system.business.semester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentPeriod {
	private UUID id;
	private UUID semesterId;
	private Instant startAt;
	private Instant endAt;
	private int minCredits;
	private int maxCredits;
}
