package com.course_registration_system.business.classcourse;

import com.course_registration_system.business.classcourse.model.ClassCourse;

import java.util.List;

public class ClassCourseService {
	private final ClassCourseRepository classCourseRepository;

	public ClassCourseService(ClassCourseRepository classCourseRepository) {
		this.classCourseRepository = classCourseRepository;
	}

	public ClassCourse get(String classId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public List<ClassCourse> list() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public ClassCourse create(ClassCourse classCourse) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
