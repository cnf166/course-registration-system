package com.course_registration_system.business.enrollment.model;

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
public class Enrollment {
	private UUID id;
	private UUID studentId;
	private String classId;
	private Instant enrolledAt;
	private String status;
}
