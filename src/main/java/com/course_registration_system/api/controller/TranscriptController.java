package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.response.TranscriptResponse;
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
@RequestMapping("/api/v1/transcripts")
@Tag(name = "Transcripts", description = "Academic results of the signed-in student")
public class TranscriptController {

    @GetMapping("/me")
    @Operation(
            summary = "Get my transcript",
            description = "[NOT BACKED] Returns the accumulated transcript with the GPA/CPA of previous "
                    + "semesters. The data is also used to validate prerequisite conditions. "
                    + "Grades are not stored yet. Permission: STUDENT.")
    @ApiResponse(responseCode = "200", description = "The transcript")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not a STUDENT",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<TranscriptResponse> getMyTranscript() {
        throw new ApiNotImplementedException();
    }
}
