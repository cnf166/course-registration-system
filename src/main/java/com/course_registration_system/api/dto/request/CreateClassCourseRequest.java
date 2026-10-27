package com.course_registration_system.api.dto.request;

import com.course_registration_system.api.dto.response.ScheduleSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Data required to create a new course class")
public record CreateClassCourseRequest(
        @Schema(description = "Display name of the class", example = "Software Architecture")
        @NotBlank
        String name,

        @Schema(description = "Number of credits", example = "3", minimum = "1")
        @NotNull
        @Min(1)
        Integer credits,

        @Schema(description = "Maximum number of students", example = "60", minimum = "1")
        @NotNull
        @Min(1)
        Integer capacity,

        @Schema(description = "Weekly sessions of the class; at least one slot required")
        @NotEmpty
        List<@Valid @NotNull ScheduleSlot> schedule
) {
}
