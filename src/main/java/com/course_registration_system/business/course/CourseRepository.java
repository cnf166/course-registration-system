package com.course_registration_system.business.course;

import com.course_registration_system.business.course.model.Course;

import java.util.Optional;

public interface CourseRepository {
	Optional<Course> findByCode(String code);
	boolean existsByCode(String code);
	Course save(Course course);
}
