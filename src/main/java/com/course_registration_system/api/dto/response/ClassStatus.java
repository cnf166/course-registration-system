package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "[NOT BACKED] Status of a course class. Not stored yet.")
public enum ClassStatus {
    OPEN,
    CLOSED,
    CANCELLED // not enough students
}
