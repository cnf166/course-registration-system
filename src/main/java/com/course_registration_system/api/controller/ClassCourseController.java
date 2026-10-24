package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.CreateClassCourseRequest;
import com.course_registration_system.api.dto.request.UpdateClassStatusRequest;
import com.course_registration_system.api.dto.response.ClassCourseDetailResponse;
import com.course_registration_system.api.dto.response.ClassCourseSummaryResponse;
import com.course_registration_system.api.dto.response.PageResponse;
import com.course_registration_system.api.error.ApiNotImplementedException;
import com.course_registration_system.api.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/course-classes")
@Tag(name = "Course Classes", description = "Classes open for registration and class management")
public class ClassCourseController {

    @GetMapping
    @Operation(
            summary = "Search open classes",
            description = "Returns a page of classes offered in a semester (the current semester when "
                    + "semesterId is omitted). Supports filtering by course code, day of the week and "
                    + "instructor. Permission: Public / Auth.")
    @ApiResponse(responseCode = "200", description = "A page of classes")
    @ApiResponse(responseCode = "400", description = "A query parameter is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PageResponse<ClassCourseSummaryResponse>> searchClassCoursees(
            @Parameter(description = "Semester to search in; defaults to the current semester")
            @RequestParam(required = false) UUID semesterId,
            @Parameter(description = "Filter by course code", example = "INT3105")
            @RequestParam(required = false) String courseCode,
            @Parameter(description = "Filter by day of the week of a session")
            @RequestParam(required = false) DayOfWeek dayOfWeek,
            @Parameter(description = "[NOT BACKED] Filter by instructor. Not stored yet, so it has no effect.")
            @RequestParam(required = false) String instructor,
            @Parameter(description = "Zero-based page index")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size (1 to 100)")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        throw new ApiNotImplementedException();
    }

    @GetMapping("/{classId}")
    @Operation(
            summary = "Get class details",
            description = "Returns the schedule, room, instructor and the current and maximum number of "
                    + "students (enrolledCount / capacity) of a class. Permission: Public / Auth.")
    @ApiResponse(responseCode = "200", description = "Class details")
    @ApiResponse(responseCode = "404", description = "Class not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ClassCourseDetailResponse> getClassCourse(
            @Parameter(description = "Class ID", example = "INT3105 1")
            @PathVariable String classId) {
        throw new ApiNotImplementedException();
    }

    @PostMapping
    @Operation(
            summary = "Create a class",
            description = "Creates a new class with its weekly schedule and room. Permission: ADMIN.")
    @ApiResponse(responseCode = "201", description = "Class created")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not an ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Course or semester not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "A class with this ID already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ClassCourseDetailResponse> createClassCourse(
            @Valid @RequestBody CreateClassCourseRequest request) {
        throw new ApiNotImplementedException();
    }

    @PutMapping("/{classId}/status")
    @Operation(
            summary = "Change class status",
            description = "[NOT BACKED] Manually closes or reopens a class, or cancels it when it does not "
                    + "reach the minimum number of students. The class status is not stored yet. "
                    + "Permission: ADMIN.")
    @ApiResponse(responseCode = "200", description = "Status changed")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not an ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Class not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "The status change is not allowed for this class",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ClassCourseDetailResponse> updateClassStatus(
            @Parameter(description = "Class ID", example = "INT3105 1")
            @PathVariable String classId,
            @Valid @RequestBody UpdateClassStatusRequest request) {
        throw new ApiNotImplementedException();
    }
}
