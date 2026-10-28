package com.course_registration_system.business.exception;

// A registration rule is not met: no open enrollment period, credit limit exceeded.
public class RuleViolationException extends BusinessException {

	public RuleViolationException(String code, String message) {
		super(code, message);
	}

	// Backward-compatible overload: uses code as message when message is not explicitly provided
	public RuleViolationException(String code) {
		super(code, code);
	}
}
