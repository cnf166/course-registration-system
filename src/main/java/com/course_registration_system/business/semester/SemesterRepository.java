package com.course_registration_system.business.semester;

import com.course_registration_system.business.semester.model.Semester;

import java.util.Optional;
import java.util.UUID;

public interface SemesterRepository {
	Optional<Semester> findById(UUID id);
	Optional<Semester> findCurrent();
}
