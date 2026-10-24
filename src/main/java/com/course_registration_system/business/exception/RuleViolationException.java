package com.course_registration_system.business.exception;

// A registration rule is not met: no open enrollment period, credit limit exceeded.
public class RuleViolationException extends BusinessException {

	public RuleViolationException(String message) {
		super(message);
	}
}
