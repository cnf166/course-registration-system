package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enrollment result for a single class in a bulk request")
public record EnrollResultItem(
        @Schema(description = "Class ID this result applies to")
        String classId,

        @Schema(description = "Outcome for this class",
                allowableValues = {"ENROLLED", "REJECTED", "RETRY"},
                example = "ENROLLED")
        String status,

        @Schema(description = "Rejection reason code; null when status is ENROLLED or RETRY",
                allowableValues = {
                        "ENROLLMENT_PERIOD_CLOSED",
                        "CLASS_NOT_FOUND",
                        "CLASS_FULL",
                        "ALREADY_ENROLLED",
                        "SCHEDULE_CONFLICT",
                        "CREDIT_LIMIT_EXCEEDED"
                },
                nullable = true,
                example = "CLASS_FULL")
        String reason
) {
}
