package com.course_registration_system.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "A page of results")
public record PageResponse<T>(
        @Schema(description = "Items on this page")
        List<T> content,

        @Schema(description = "Zero-based page index", example = "0")
        int page,

        @Schema(description = "Requested page size", example = "20")
        int size,

        @Schema(description = "Total number of items across all pages", example = "135")
        long totalElements,

        @Schema(description = "Total number of pages", example = "7")
        int totalPages
) {
}
