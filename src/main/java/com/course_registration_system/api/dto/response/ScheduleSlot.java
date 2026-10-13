package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.DayOfWeek;

@Schema(description = "One weekly session of a class: a day of the week paired with a shift")
public record ScheduleSlot(
        @Schema(description = "Day of the week; null when the class has no fixed day", example = "MONDAY",
                nullable = true)
        DayOfWeek day,

        @Schema(description = "Shift of the day (1 to 4); null when the class has no specific shift", example = "2",
                minimum = "1", maximum = "4", nullable = true)
        @Min(1)
        @Max(4)
        Integer shift
) {
}
