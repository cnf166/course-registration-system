package com.course_registration_system.business.courseclass;

import com.course_registration_system.business.courseclass.model.CourseClass;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseClassRepository {
	Optional<CourseClass> findById(String id);
	// courseCode and day are optional filters; null means no filter.
	List<CourseClass> findBySemester(UUID semesterId, String courseCode, DayOfWeek day);
	boolean existsById(String id);
	CourseClass save(CourseClass courseClass);
}
