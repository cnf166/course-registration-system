package com.course_registration_system.api.dto.request;

import com.course_registration_system.api.dto.response.ClassStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "[NOT BACKED] Request to open, close or cancel a class. The class status is not stored yet.")
public record UpdateClassStatusRequest(
        @Schema(description = "New status of the class")
        @NotNull
        ClassStatus status,

        @Schema(description = "Why the status changes", example = "Not enough students", nullable = true)
        String reason
) {
}
