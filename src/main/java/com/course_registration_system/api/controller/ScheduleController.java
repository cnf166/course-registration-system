package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.response.ScheduleResponse;
import com.course_registration_system.api.error.ApiNotImplementedException;
import com.course_registration_system.api.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedules")
@Tag(name = "Schedules", description = "Personal timetable of the signed-in student")
public class ScheduleController {

    @GetMapping("/me")
    @Operation(
            summary = "Get my timetable",
            description = "Returns the recurring weekly timetable of the signed-in student in the current "
                    + "semester, built from the classes they have registered for. Permission: STUDENT.")
    @ApiResponse(responseCode = "200", description = "The weekly timetable")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not a STUDENT",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ScheduleResponse> getMySchedule() {
        throw new ApiNotImplementedException();
    }
}
