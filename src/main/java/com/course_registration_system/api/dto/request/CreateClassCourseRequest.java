package com.course_registration_system.api.dto.request;

import com.course_registration_system.api.dto.response.ScheduleSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Schema(description = "Data of a new course class, with its schedule and room")
public record CreateCourseClassRequest(
        @Schema(description = "Class ID, assigned by the admin and unique", example = "INT3105 1")
        @NotBlank
        String classId,

        @Schema(description = "Code of the course this class belongs to", example = "INT3105")
        @NotBlank
        String courseCode,

        @Schema(description = "Semester in which the class is offered")
        @NotNull
        UUID semesterId,

        @Schema(description = "Section label of the class", example = "01", nullable = true)
        String section,

        @Schema(description = "Weekly sessions of the class")
        @NotEmpty
        List<@Valid @NotNull ScheduleSlot> schedule,

        @Schema(description = "Room the class is held in", example = "106A", nullable = true)
        String room,

        @Schema(description = "Maximum number of students", example = "60", minimum = "30")
        @NotNull
        @Min(30)
        Integer capacity,

        @Schema(description = "[NOT BACKED] Instructor of the class. Not stored yet.", nullable = true)
        String instructor
) {
}
