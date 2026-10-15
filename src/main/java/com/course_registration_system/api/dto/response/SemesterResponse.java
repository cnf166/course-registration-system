package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "An academic semester")
public record SemesterResponse(
        @Schema(description = "Semester ID")
        UUID semesterId,

        @Schema(description = "Academic year", example = "2026")
        int year,

        @Schema(description = "Term within the year", example = "1")
        int term,

        @Schema(description = "First day of the semester", example = "2026-09-07")
        LocalDate startDate,

        @Schema(description = "Last day of the semester", example = "2027-01-17")
        LocalDate endDate,

        @Schema(description = "Whether this is the currently active semester", example = "true")
        boolean current
) {
}
