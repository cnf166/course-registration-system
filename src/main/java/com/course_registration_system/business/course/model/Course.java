package com.course_registration_system.business.course.model;

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
public class Course {
	private String code;
	private String title;
	private String description;
	private int credits;
	private UUID createdBy;
	private Instant createdAt;
}
