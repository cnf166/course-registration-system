package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.ChangePasswordRequest;
import com.course_registration_system.api.dto.response.UserProfileResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Profile", description = "Profile and password of the signed-in user")
public class UserController {

    @GetMapping("/me")
    @Operation(
            summary = "Get my profile",
            description = "Returns the profile of the signed-in user: name, student ID, major and role. "
                    + "The user is identified from the JWT. Permission: Authenticated.")
    @ApiResponse(responseCode = "200", description = "Profile of the signed-in user")
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        throw new ApiNotImplementedException();
    }

    @PutMapping("/me/password")
    @Operation(
            summary = "Change my password",
            description = "Changes the password of the signed-in user. Permission: Authenticated.")
    @ApiResponse(responseCode = "204", description = "Password changed")
    @ApiResponse(responseCode = "400", description = "Request body is invalid or the current password is incorrect",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        throw new ApiNotImplementedException();
    }
}
