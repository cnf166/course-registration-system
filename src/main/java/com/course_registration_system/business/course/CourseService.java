package com.course_registration_system.business.course;

import com.course_registration_system.business.course.model.Course;

import java.util.UUID;

public class CourseService {

	private final CourseRepository courseRepository;

	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public Course get(String code) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Course create(Course course, UUID adminId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public Course update(String code, Course course) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
