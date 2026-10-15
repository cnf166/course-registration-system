package com.course_registration_system.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to change the password of the signed-in user")
public record ChangePasswordRequest(
        @Schema(description = "Current password", format = "password")
        @NotBlank
        String currentPassword,

        @Schema(description = "New password (8 to 72 characters)", format = "password", minLength = 8, maxLength = 72)
        @NotBlank
        @Size(min = 8, max = 72)
        String newPassword
) {

    @Override
    public String toString() {
        return "ChangePasswordRequest[currentPassword=***, newPassword=***]";
    }
}
