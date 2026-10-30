package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "The single enrollment period for all students")
public record EnrollmentPeriodResponse(
        @Schema(description = "When registration opens (with timezone offset)",
                example = "2026-11-01T08:00:00+07:00")
        OffsetDateTime startAt,

        @Schema(description = "When registration closes (exclusive, with timezone offset)",
                example = "2026-11-03T17:00:00+07:00")
        OffsetDateTime endAt,

        @Schema(description = "Computed status of the period",
                allowableValues = {"UPCOMING", "OPEN", "CLOSED"},
                example = "OPEN")
        String status
) {
}
