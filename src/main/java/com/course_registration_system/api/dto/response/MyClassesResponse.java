package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Classes the student has successfully registered for in a semester")
public record MyClassesResponse(
        @Schema(description = "Semester the classes belong to")
        UUID semesterId,

        @Schema(description = "Registered classes")
        List<EnrollmentResponse> classes,

        @Schema(description = "Total credits of the registered classes in this semester", example = "15")
        int totalCredits
) {
}
