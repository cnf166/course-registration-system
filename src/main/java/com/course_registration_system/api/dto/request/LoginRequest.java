package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials used to sign in")
public record LoginRequest(
        @Schema(description = "Username (email or student ID)", example = "24020000")
        @NotBlank
        String username,

        @Schema(description = "Account password", format = "password")
        @NotBlank
        String password
) {

    @Override
    public String toString() {
        return "LoginRequest[username=" + username + ", password=***]";
    }
}
