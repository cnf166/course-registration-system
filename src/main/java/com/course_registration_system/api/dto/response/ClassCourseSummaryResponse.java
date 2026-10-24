package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Course class as shown in the class list")
public record ClassCourseSummaryResponse(
        @Schema(description = "Class ID", example = "INT3105 1")
        String classId,

        @Schema(description = "Code of the course", example = "INT3105")
        String courseCode,

        @Schema(description = "Title of the course", example = "Software Architecture")
        String courseTitle,

        @Schema(description = "Section label", example = "01", nullable = true)
        String section,

        @Schema(description = "Semester in which the class is offered")
        UUID semesterId,

        @Schema(description = "Weekly sessions of the class")
        List<ScheduleSlot> schedule,

        @Schema(description = "Room", example = "106A", nullable = true)
        String room,

        @Schema(description = "Current number of enrolled students", example = "50")
        int enrolledCount,

        @Schema(description = "Maximum number of students", example = "60")
        int capacity,

        @Schema(description = "[NOT BACKED] Instructor of the class. Not stored yet.", nullable = true)
        String instructor,

        @Schema(description = "[NOT BACKED] Status of the class. Not stored yet.",
                allowableValues = {"OPEN", "CLOSED", "CANCELLED"}, nullable = true)
        String status
) {
}
