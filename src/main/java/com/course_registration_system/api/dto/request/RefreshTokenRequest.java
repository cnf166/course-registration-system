package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Refresh token used to obtain a new access token")
public record RefreshTokenRequest(
        @Schema(description = "Refresh token issued at login")
        @NotBlank
        String refreshToken
) {

    @Override
    public String toString() {
        return "RefreshTokenRequest[refreshToken=***]";
    }
}
