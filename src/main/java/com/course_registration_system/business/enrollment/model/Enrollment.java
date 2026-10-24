package com.course_registration_system.business.enrollment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
	private String id;
	private String studentId;
	private String classId;
	private Instant enrolledAt;
}
