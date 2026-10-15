package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;

@Schema(description = "One weekly session of a registered class")
public record ScheduleEntryResponse(
        @Schema(description = "Day of the week; null when the class has no fixed day", example = "MONDAY",
                nullable = true)
        DayOfWeek day,

        @Schema(description = "Shift of the day (1 to 4); null when the class has no specific shift", example = "2",
                minimum = "1", maximum = "4", nullable = true)
        Integer shift,

        @Schema(description = "Class ID", example = "INT3105 1")
        String classId,

        @Schema(description = "Code of the course", example = "INT3105")
        String courseCode,

        @Schema(description = "Title of the course", example = "Software Architecture")
        String courseTitle,

        @Schema(description = "Room", example = "106A", nullable = true)
        String room
) {
}
