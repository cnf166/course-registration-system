package com.course_registration_system.business.user;

import com.course_registration_system.business.user.model.User;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findById(String id);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	// Saving a user whose username is taken by another user replaces that user; check existsByUsername first.
	User save(User user);
}
