package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "One weekly session of a class: a day of the week paired with a shift")
public record ScheduleSlotDto(
        @Schema(description = "Day of the week (1=Sunday, 2=Monday, … 7=Saturday)", example = "2",
                minimum = "1", maximum = "7")
        @Min(1) @Max(7)
        int dayOfWeek,

        @Schema(description = "Shift of the day (1 to 4)", example = "1",
                minimum = "1", maximum = "4")
        @Min(1) @Max(4)
        int shift
) {
}
