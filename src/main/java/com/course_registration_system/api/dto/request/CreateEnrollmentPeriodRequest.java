package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Data of a new enrollment period")
public record CreateEnrollmentPeriodRequest(
        @Schema(description = "Semester the period belongs to")
        @NotNull
        UUID semesterId,

        @Schema(description = "When registration opens (UTC); must be before endAt", example = "2026-08-20T01:00:00Z")
        @NotNull
        Instant startAt,

        @Schema(description = "When registration closes (UTC)", example = "2026-08-27T16:59:00Z")
        @NotNull
        Instant endAt,

        @Schema(description = "Minimum number of credits a student must register; must not exceed maxCredits",
                example = "14", minimum = "0")
        @NotNull
        @Min(0)
        Integer minCredits,

        @Schema(description = "Maximum number of credits a student may register", example = "24", minimum = "1")
        @NotNull
        @Min(1)
        Integer maxCredits,

        @Schema(description = "[NOT BACKED] Student cohort this opening window applies to. Not stored yet.",
                nullable = true)
        String cohort,

        @Schema(description = "[NOT BACKED] Faculty this opening window applies to. Not stored yet.",
                nullable = true)
        String faculty
) {

    // Cross-field rules. Null values are reported by the @NotNull checks above, so they pass here.
    @Schema(hidden = true)
    @AssertTrue(message = "startAt must be before endAt")
    public boolean isTimeRangeValid() {
        return startAt == null || endAt == null || startAt.isBefore(endAt);
    }

    @Schema(hidden = true)
    @AssertTrue(message = "minCredits must not be greater than maxCredits")
    public boolean isCreditRangeValid() {
        return minCredits == null || maxCredits == null || minCredits <= maxCredits;
    }
}
