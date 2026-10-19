package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Course as shown in the course catalog list")
public record CourseSummaryResponse(
        @Schema(description = "Course code", example = "INT3105")
        String courseCode,

        @Schema(description = "Course title", example = "Software Architecture")
        String title,

        @Schema(description = "Number of credits", example = "3")
        int credits
) {
}
