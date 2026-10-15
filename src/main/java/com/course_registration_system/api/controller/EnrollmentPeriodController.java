package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.CreateEnrollmentPeriodRequest;
import com.course_registration_system.api.dto.response.EnrollmentPeriodResponse;
import com.course_registration_system.api.error.ApiNotImplementedException;
import com.course_registration_system.api.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollment-periods")
@Tag(name = "Enrollment Periods", description = "Periods during which course registration is open")
public class EnrollmentPeriodController {

    @GetMapping("/active")
    @Operation(
            summary = "List open enrollment periods",
            description = "Returns the enrollment periods that are currently open, with the opening window "
                    + "for each cohort/faculty. The list is empty when no period is open. "
                    + "Permission: Public / Auth.")
    @ApiResponse(responseCode = "200", description = "Open enrollment periods (possibly empty)")
    public ResponseEntity<List<EnrollmentPeriodResponse>> getActiveEnrollmentPeriods() {
        throw new ApiNotImplementedException();
    }

    @PostMapping
    @Operation(
            summary = "Create an enrollment period",
            description = "Sets up a registration period: start time, end time, and the minimum and maximum "
                    + "credits a student may register. Permission: ADMIN.")
    @ApiResponse(responseCode = "201", description = "Enrollment period created")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not an ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Semester not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<EnrollmentPeriodResponse> createEnrollmentPeriod(
            @Valid @RequestBody CreateEnrollmentPeriodRequest request) {
        throw new ApiNotImplementedException();
    }
}
