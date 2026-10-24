package com.course_registration_system.business.classcourse;

import com.course_registration_system.business.classcourse.model.ClassCourse;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClassCourseRepository {
	Optional<ClassCourse> findById(String id);

	List<ClassCourse> findAll();

	// Ids that don't exist are skipped.
	List<ClassCourse> findByIds(Collection<String> ids);

	boolean existsById(String id);

	ClassCourse save(ClassCourse classCourse);
}
