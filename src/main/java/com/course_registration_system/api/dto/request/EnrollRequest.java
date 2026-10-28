package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

// The student is identified from the JWT, so the request carries only the class.
@Schema(description = "Request to register for one or more classes; the student is identified from the access token")
public record EnrollRequest(
        @Schema(description = "IDs of the classes to register for")
        @NotEmpty
        List<@NotBlank String> classIds
) {
}
