package com.course_registration_system.business.exception;

public class InvalidInputException extends BusinessException {

	public InvalidInputException(String message) {
		super("VALIDATION_ERROR", message);
	}

	public InvalidInputException(String code, String message) {
		super(code, message);
	}
}