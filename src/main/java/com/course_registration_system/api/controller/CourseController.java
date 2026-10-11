package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.CreateCourseRequest;
import com.course_registration_system.api.dto.request.UpdateCourseRequest;
import com.course_registration_system.api.dto.response.CourseDetailResponse;
import com.course_registration_system.api.dto.response.CourseSummaryResponse;
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

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Course catalog and course management")
public class CourseController {

    @GetMapping
    @Operation(
            summary = "Search the course catalog",
            description = "Returns a page of courses. Supports filtering by faculty, credits and keyword. "
                    + "Permission: Public / Auth.")
    @ApiResponse(responseCode = "200", description = "A page of courses")
    @ApiResponse(responseCode = "400", description = "A query parameter is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PageResponse<CourseSummaryResponse>> searchCourses(
            @Parameter(description = "[NOT BACKED] Filter by faculty. Not stored yet, so it has no effect.")
            @RequestParam(required = false) String faculty,
            @Parameter(description = "Filter by number of credits", example = "3")
            @RequestParam(required = false) @Min(2) Integer credits,
            @Parameter(description = "Keyword matched against course code and title", example = "software")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Zero-based page index")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size (1 to 100)")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        throw new ApiNotImplementedException();
    }

    @GetMapping("/{courseCode}")
    @Operation(
            summary = "Get course details",
            description = "Returns the details of a course, including its prerequisite and corequisite courses. "
                    + "Permission: Public / Auth.")
    @ApiResponse(responseCode = "200", description = "Course details")
    @ApiResponse(responseCode = "404", description = "Course not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CourseDetailResponse> getCourse(
            @Parameter(description = "Course code", example = "SE101")
            @PathVariable String courseCode) {
        throw new ApiNotImplementedException();
    }

    @PostMapping
    @Operation(
            summary = "Create a course",
            description = "Adds a new course to the system. Permission: ADMIN.")
    @ApiResponse(responseCode = "201", description = "Course created")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not an ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "A course with this code already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CourseDetailResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        throw new ApiNotImplementedException();
    }

    @PutMapping("/{courseCode}")
    @Operation(
            summary = "Update a course",
            description = "Updates the information, credits and prerequisite conditions of a course. "
                    + "Permission: ADMIN.")
    @ApiResponse(responseCode = "200", description = "Course updated")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not an ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Course not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CourseDetailResponse> updateCourse(
            @Parameter(description = "Course code", example = "SE101")
            @PathVariable String courseCode,
            @Valid @RequestBody UpdateCourseRequest request) {
        throw new ApiNotImplementedException();
    }
}
