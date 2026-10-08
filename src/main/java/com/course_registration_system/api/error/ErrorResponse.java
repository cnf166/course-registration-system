package com.course_registration_system.api.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Standard error body returned by every endpoint on failure")
public record ErrorResponse(
        @Schema(description = "When the error occurred (UTC)", example = "2026-10-06T08:30:00Z")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "HTTP reason phrase", example = "Bad Request")
        String error,

        @Schema(description = "Human-readable explanation of the error", example = "Validation failed")
        String message,

        @Schema(description = "Request path that caused the error", example = "/api/v1/enrollments")
        String path,

        @Schema(description = "Per-field validation errors; empty when the error is not a validation error")
        List<FieldError> fieldErrors
) {

    @Schema(description = "A single field-level validation error")
    public record FieldError(
            @Schema(description = "Name of the invalid field", example = "classId")
            String field,

            @Schema(description = "Why the field is invalid", example = "must not be blank")
            String message
    ) {
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path, List.of());
    }

    public static ErrorResponse of(int status, String error, String message, String path,
                                   List<FieldError> fieldErrors) {
        return new ErrorResponse(Instant.now(), status, error, message, path, fieldErrors);
    }
}
