package com.course_registration_system.business.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SystemAdmin {
	private UUID id;

	public SystemAdmin() {
	}

	public SystemAdmin(UUID id) {
		this.id = id;
	}
}
