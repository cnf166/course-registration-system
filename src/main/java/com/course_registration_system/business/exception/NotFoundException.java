package com.course_registration_system.business.exception;

public class NotFoundException extends BusinessException {

	public NotFoundException(String message) {
		super("NOT_FOUND", message);
	}

	public NotFoundException(String code, String message) {
		super(code, message);
	}
}
