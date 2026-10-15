package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Recurring weekly timetable of the student, built from the classes they have registered for")
public record ScheduleResponse(
        @Schema(description = "Semester the timetable belongs to (the current semester)")
        UUID semesterId,

        @Schema(description = "Weekly sessions, ordered by day of the week and then by shift")
        List<ScheduleEntryResponse> entries
) {
}
