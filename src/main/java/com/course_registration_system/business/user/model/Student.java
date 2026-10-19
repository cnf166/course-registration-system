package com.course_registration_system.business.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Student {
	private UUID id;
	private String major;
	private String studentId;

	public Student() {
	}

	public Student(UUID id, String major, String studentId) {
		this.id = id;
		this.major = major;
		this.studentId = studentId;
	}
}
