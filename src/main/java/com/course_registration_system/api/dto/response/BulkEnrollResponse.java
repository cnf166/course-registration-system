package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Results of a bulk enrollment request; one entry per requested classId")
public record BulkEnrollResponse(
        @Schema(description = "Per-class enrollment results")
        List<EnrollResultItem> results
) {
}
