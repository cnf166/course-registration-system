package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Full details of a course")
public record CourseDetailResponse(
        @Schema(description = "Course code", example = "INT3105")
        String courseCode,

        @Schema(description = "Course title", example = "Software Architecture")
        String title,

        @Schema(description = "Course description", nullable = true)
        String description,

        @Schema(description = "Number of credits", example = "3")
        int credits,

        @Schema(description = "[NOT BACKED] Course codes of the prerequisite courses. Not stored yet.")
        List<String> prerequisites,

        @Schema(description = "When the course was created (UTC)")
        Instant createdAt
) {
}
