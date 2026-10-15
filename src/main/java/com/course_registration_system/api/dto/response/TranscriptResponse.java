package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "[NOT BACKED] Accumulated transcript with GPA/CPA of previous semesters. "
        + "Grades are not stored yet.")
public record TranscriptResponse(
        @Schema(description = "Student ID", example = "24020000")
        String studentId,

        @Schema(description = "Results of each previous semester")
        List<TranscriptSemesterResponse> semesters,

        @Schema(description = "Cumulative GPA (CPA) over all semesters on a 4-point scale", example = "3.2")
        double cpa,

        @Schema(description = "Total credits earned over all semesters", example = "45")
        int totalEarnedCredits
) {
}
