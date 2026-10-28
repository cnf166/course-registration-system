package com.course_registration_system.business.classcourse.model;

public record ClassCourseWithStats(ClassCourse classCourse, int enrolledCount) {

	public int availableSeats() {
		return Math.max(0, classCourse.getCapacity() - enrolledCount);
	}
}