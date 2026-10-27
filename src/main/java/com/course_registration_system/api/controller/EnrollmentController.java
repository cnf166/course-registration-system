package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.EnrollRequest;
import com.course_registration_system.api.dto.response.BulkEnrollResponse;
import com.course_registration_system.api.dto.response.MyClassesResponse;
import com.course_registration_system.api.error.ApiNotImplementedException;
import com.course_registration_system.api.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enrollments")
@Tag(name = "Enrollments", description = "Course registration of the signed-in student")
public class EnrollmentController {

    @PostMapping
    @Operation(
            summary = "Register for one or more classes",
            description = "Registers the signed-in student for the given class IDs. "
                    + "Each class is evaluated independently; a rejection for one class "
                    + "does not affect the others. Permission: STUDENT.")
    @ApiResponse(responseCode = "200", description = "Request processed; see each result for individual outcomes")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not a STUDENT",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "503", description = "System temporarily unable to process safely; retry later",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<BulkEnrollResponse> enroll(@Valid @RequestBody EnrollRequest request) {
        throw new ApiNotImplementedException();
    }

    @DeleteMapping("/{classId}")
    @Operation(
            summary = "Cancel a registration",
            description = "Cancels a class the signed-in student registered for, within the allowed time, "
                    + "and releases the slot. Permission: STUDENT.")
    @ApiResponse(responseCode = "204", description = "Registration cancelled")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not a STUDENT",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "The student is not registered for this class",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Enrollment period is not open",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> cancelEnrollment(
            @Parameter(description = "ID of the class to cancel")
            @PathVariable String classId) {
        throw new ApiNotImplementedException();
    }

    @GetMapping("/my-classes")
    @Operation(
            summary = "List my registered classes",
            description = "Returns all classes the signed-in student has successfully enrolled in, "
                    + "with total credits. Use this to confirm enrollment after a timeout. Permission: STUDENT.")
    @ApiResponse(responseCode = "200", description = "Enrolled classes and total credits")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not a STUDENT",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<MyClassesResponse> getMyClasses() {
        throw new ApiNotImplementedException();
    }
}
