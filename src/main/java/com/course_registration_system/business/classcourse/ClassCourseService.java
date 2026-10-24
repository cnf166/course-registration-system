package com.course_registration_system.business.courseclass;

import com.course_registration_system.business.courseclass.model.CourseClass;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public class CourseClassService {

	private final CourseClassRepository courseClassRepository;

	public CourseClassService(CourseClassRepository courseClassRepository) {
		this.courseClassRepository = courseClassRepository;
	}

	public CourseClass get(String classId) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public List<CourseClass> listBySemester(UUID semesterId, String courseCode, DayOfWeek day) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public CourseClass create(CourseClass courseClass) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
