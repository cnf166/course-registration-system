package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Schema(description = "New open/close times for the enrollment period")
public record CreateEnrollmentPeriodRequest(
        @Schema(description = "When registration opens (with timezone offset); must be before endAt",
                example = "2026-11-01T08:00:00+07:00")
        @NotNull
        OffsetDateTime startAt,

        @Schema(description = "When registration closes (exclusive, with timezone offset)",
                example = "2026-11-03T17:00:00+07:00")
        @NotNull
        OffsetDateTime endAt
) {

    // Cross-field rules. Null values are reported by the @NotNull checks above, so they pass here.
    @Schema(hidden = true)
    @AssertTrue(message = "startAt must be before endAt")
    public boolean isTimeRangeValid() {
        return startAt == null || endAt == null || startAt.isBefore(endAt);
    }
}
