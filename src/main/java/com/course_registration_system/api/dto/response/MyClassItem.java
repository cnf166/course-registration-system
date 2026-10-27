package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A class the student is currently enrolled in")
public record MyClassItem(
        @Schema(description = "System-generated class ID")
        String classId,

        @Schema(description = "Display name of the class")
        String name,

        @Schema(description = "Number of credits", example = "3")
        int credits,

        @Schema(description = "Enrollment status; always ENROLLED for rows returned by this endpoint",
                example = "ENROLLED")
        String status
) {
}
