package com.course_registration_system.business.exception;

public class UnauthorizedException extends BusinessException {
	public UnauthorizedException(String code, String message) {
		super(code, message);
	}
}
