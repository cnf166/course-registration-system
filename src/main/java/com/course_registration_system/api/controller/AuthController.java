package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.LoginRequest;
import com.course_registration_system.api.dto.request.RefreshTokenRequest;
import com.course_registration_system.api.dto.response.TokenResponse;
import com.course_registration_system.api.error.ErrorResponse;
import com.course_registration_system.business.auth.AuthService;
import com.course_registration_system.business.auth.model.AuthTokens;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Sign in and token refresh (public endpoints)")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Sign in",
            description = "Signs in with a student ID or email and a password. "
                    + "Returns a JWT access token and a refresh token. Permission: Public.")
    @ApiResponse(responseCode = "200", description = "Signed in successfully")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Identifier or password is incorrect",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(toResponse(authService.login(request.username(), request.password())));
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh access token",
            description = "Issues a new access token (and refresh token) when the old access token has expired. "
                    + "Permission: Public.")
    @ApiResponse(responseCode = "200", description = "New tokens issued")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Refresh token is invalid or expired",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(toResponse(authService.refresh(request.refreshToken())));
    }

    private static TokenResponse toResponse(AuthTokens tokens) {
        return new TokenResponse(tokens.accessToken(), tokens.refreshToken(), "Bearer", tokens.expiresIn());
    }
}
