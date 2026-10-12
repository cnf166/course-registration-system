package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "A period during which students can register for courses")
public record EnrollmentPeriodResponse(
        @Schema(description = "Enrollment period ID")
        UUID periodId,

        @Schema(description = "Semester this period belongs to")
        UUID semesterId,

        @Schema(description = "When registration opens (UTC)", example = "2026-08-20T01:00:00Z")
        Instant startAt,

        @Schema(description = "When registration closes (UTC)", example = "2026-08-27T16:59:00Z")
        Instant endAt,

        @Schema(description = "Minimum number of credits a student must register", example = "14")
        int minCredits,

        @Schema(description = "Maximum number of credits a student may register", example = "24")
        int maxCredits,

        @Schema(description = "[NOT BACKED] Student cohort this opening window applies to. Not stored yet.",
                nullable = true)
        String cohort,

        @Schema(description = "[NOT BACKED] Faculty this opening window applies to. Not stored yet.",
                nullable = true)
        String faculty
) {
}
