package com.course_registration_system.business.user;

import com.course_registration_system.business.user.model.SystemAdmin;

import java.util.Optional;
import java.util.UUID;

public interface SystemAdminRepository {
	Optional<SystemAdmin> findById(UUID id);

	SystemAdmin save(SystemAdmin systemAdmin);
}
