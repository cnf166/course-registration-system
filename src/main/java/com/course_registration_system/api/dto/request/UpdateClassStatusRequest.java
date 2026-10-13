package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "[NOT BACKED] Request to open, close or cancel a class. The class status is not stored yet.")
public record UpdateClassStatusRequest(
        @Schema(description = "New status of the class", allowableValues = {"OPEN", "CLOSED", "CANCELLED"}, example = "CLOSED")
        @NotBlank
        String status,

        @Schema(description = "Why the status changes", example = "Not enough students", nullable = true)
        String reason
) {
}
