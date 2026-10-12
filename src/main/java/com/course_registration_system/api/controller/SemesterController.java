package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.response.SemesterResponse;
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
@RequestMapping("/api/v1/semesters")
@Tag(name = "Semesters", description = "Academic semesters")
public class SemesterController {

    @GetMapping("/current")
    @Operation(
            summary = "Get the current semester",
            description = "Returns the semester that is currently active. Permission: Public / Auth.")
    @ApiResponse(responseCode = "200", description = "The current semester")
    @ApiResponse(responseCode = "404", description = "No semester is currently active",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SemesterResponse> getCurrentSemester() {
        throw new ApiNotImplementedException();
    }
}
