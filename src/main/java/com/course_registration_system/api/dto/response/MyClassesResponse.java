package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Classes the student has successfully enrolled in")
public record MyClassesResponse(
        @Schema(description = "Enrolled classes")
        List<MyClassItem> items,

        @Schema(description = "Total credits of all currently enrolled classes", example = "15")
        int totalCredits
) {
}
