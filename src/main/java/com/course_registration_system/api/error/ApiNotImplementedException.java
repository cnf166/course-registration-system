package com.course_registration_system.api.error;

public class ApiNotImplementedException extends RuntimeException {

    public ApiNotImplementedException() {
        super("This endpoint is not implemented yet");
    }
}
