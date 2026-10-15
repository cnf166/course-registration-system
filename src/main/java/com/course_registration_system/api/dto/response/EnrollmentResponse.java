package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "A class the student has registered for")
public record EnrollmentResponse(
        @Schema(description = "Enrollment ID")
        UUID enrollmentId,

        @Schema(description = "Class ID", example = "INT3105 1")
        String classId,

        @Schema(description = "Code of the course", example = "INT3105")
        String courseCode,

        @Schema(description = "Title of the course", example = "Software Architecture")
        String courseTitle,

        @Schema(description = "Number of credits of the course", example = "3")
        int credits,

        @Schema(description = "Weekly sessions of the class")
        List<ScheduleSlot> schedule,

        @Schema(description = "Room", example = "106A", nullable = true)
        String room,

        @Schema(description = "When the student registered (UTC)")
        Instant enrolledAt,

        @Schema(description = "Status of the enrollment; the set of values is not defined yet", example = "ENROLLED")
        String status
) {
}
