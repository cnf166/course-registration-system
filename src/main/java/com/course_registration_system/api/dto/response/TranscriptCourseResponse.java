package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "[NOT BACKED] A course result in the transcript. Grades are not stored yet.")
public record TranscriptCourseResponse(
        @Schema(description = "Code of the course", example = "INT3105")
        String courseCode,

        @Schema(description = "Title of the course", example = "Software Architecture")
        String title,

        @Schema(description = "Number of credits", example = "3")
        int credits,

        @Schema(description = "Score on a 10-point scale", example = "8.5")
        double score,

        @Schema(description = "Letter grade", example = "A")
        String letterGrade
) {
}
