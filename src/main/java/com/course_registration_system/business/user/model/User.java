package com.course_registration_system.business.user.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class User {
	private UUID id;
	private String name;
	private String email;
	private String passwordHash;
	private int role;
	private Instant createdAt;

	public User() {
	}

	public User(UUID id, String name, String email, String passwordHash, int role, Instant createdAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.passwordHash = passwordHash;
		this.role = role;
		this.createdAt = createdAt;
	}

}
