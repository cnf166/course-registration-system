package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tokens issued after a successful login or token refresh")
public record TokenResponse(
        @Schema(description = "JWT access token, sent as 'Authorization: Bearer <token>'")
        String accessToken,

        @Schema(description = "Refresh token used to obtain a new access token")
        String refreshToken,

        @Schema(description = "Token type", example = "Bearer")
        String tokenType,

        @Schema(description = "Access token lifetime in seconds", example = "3600")
        long expiresIn
) {

    @Override
    public String toString() {
        return "TokenResponse[accessToken=***, refreshToken=***, tokenType=" + tokenType
                + ", expiresIn=" + expiresIn + "]";
    }
}
