package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Full details of a course class")
public record ClassCourseDetailResponse(
        @Schema(description = "System-generated class ID")
        String classId,

        @Schema(description = "Display name of the class", example = "Software Architecture")
        String name,

        @Schema(description = "Number of credits", example = "3")
        int credits,

        @Schema(description = "Maximum number of students", example = "60")
        int capacity,

        @Schema(description = "Number of students currently enrolled", example = "42")
        int enrolledCount,

        @Schema(description = "Seats still available (capacity - enrolledCount)", example = "18")
        int availableSeats,

        @Schema(description = "Weekly sessions of the class")
        List<ScheduleSlotDto> schedule
) {
}
