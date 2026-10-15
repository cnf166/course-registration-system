package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

// The student is identified from the JWT, so the request carries only the class.
@Schema(description = "Request to register for a class; the student is identified from the access token")
public record EnrollRequest(
        @Schema(description = "ID of the class to register for", example = "INT3105 1")
        @NotBlank
        String classId
) {
}
