package com.course_registration_system.business.exception;

// Base of every business rule failure, so the API layer can map them to HTTP statuses in one place.
public abstract class BusinessException extends RuntimeException {

	protected BusinessException(String message) {
		super(message);
	}
}
