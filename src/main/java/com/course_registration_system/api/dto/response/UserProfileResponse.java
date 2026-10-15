package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Profile of the signed-in user")
public record UserProfileResponse(
        @Schema(description = "User ID")
        UUID userId,

        @Schema(description = "Full name", example = "Nguyen Van A")
        String name,

        @Schema(description = "Email address", example = "24020000@vnu.edu.vn")
        String email,

        @Schema(description = "Student ID; null for non-student users", example = "24020000", nullable = true)
        String studentId,

        @Schema(description = "Major; null for non-student users", example = "Computer Science", nullable = true)
        String major,

        @Schema(description = "Role of the user", allowableValues = {"STUDENT", "ADMIN"}, example = "STUDENT")
        String role
) {
}
