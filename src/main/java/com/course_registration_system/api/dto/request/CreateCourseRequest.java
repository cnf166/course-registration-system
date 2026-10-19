package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Data of a new course")
public record CreateCourseRequest(
        @Schema(description = "Course code, assigned by the admin and unique", example = "INT3105")
        @NotBlank
        String courseCode,

        @Schema(description = "Course title", example = "Software Architecture")
        @NotBlank
        String title,

        @Schema(description = "Course description", nullable = true)
        String description,

        @Schema(description = "Number of credits", example = "3", minimum = "2")
        @NotNull
        @Min(2)
        Integer credits,

        @Schema(description = "[NOT BACKED] Course codes of the prerequisite courses. Not stored yet.",
                nullable = true)
        List<String> prerequisites
) {
}
