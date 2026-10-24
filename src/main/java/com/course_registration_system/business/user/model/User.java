package com.course_registration_system.business.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private String id;
	private String username; // student code for students
	private String name;
	private String passwordHash;
	private int role; // as in data/define/UserRole.java
	private Instant createdAt;
}
