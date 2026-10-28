package com.course_registration_system.business.exception;

// The request clashes with current state: class full, already registered, timetable clash, duplicate code.
public class ConflictException extends BusinessException {

	public ConflictException(String code, String message) {
		super(code, message);
	}

	// Backward-compatible overload: uses code as message when message is not explicitly provided
	public ConflictException(String code) {
		super(code, code);
	}
}
