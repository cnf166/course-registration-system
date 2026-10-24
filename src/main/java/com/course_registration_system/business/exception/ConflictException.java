package com.course_registration_system.business.exception;

// The request clashes with current state: class full, already registered, timetable clash, duplicate code.
public class ConflictException extends BusinessException {

	public ConflictException(String message) {
		super(message);
	}
}
