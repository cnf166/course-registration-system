package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "[NOT BACKED] Results of one semester in the transcript. Grades are not stored yet.")
public record TranscriptSemesterResponse(
        @Schema(description = "Semester ID")
        UUID semesterId,

        @Schema(description = "Academic year", example = "2026")
        int year,

        @Schema(description = "Term within the year", example = "1")
        int term,

        @Schema(description = "Course results of the semester")
        List<TranscriptCourseResponse> courses,

        @Schema(description = "GPA of this semester on a 4-point scale", example = "3.4")
        double gpa,

        @Schema(description = "Credits earned in this semester", example = "15")
        int earnedCredits
) {
}
