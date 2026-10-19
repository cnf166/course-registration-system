package com.course_registration_system.business.user;

import com.course_registration_system.business.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
	Optional<User> findById(UUID id);
	Optional<User> findByEmail(String email);
	User save(User user);
}
