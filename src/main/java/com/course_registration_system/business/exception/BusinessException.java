package com.course_registration_system.business.exception;

// Base of every business rule failure, so the API layer can map them to HTTP statuses in one place.
public abstract class BusinessException extends RuntimeException {

	private final String code;

	protected BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
