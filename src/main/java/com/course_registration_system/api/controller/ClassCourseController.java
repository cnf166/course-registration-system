package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.CreateClassCourseRequest;
import com.course_registration_system.api.dto.response.ClassCourseDetailResponse;
import com.course_registration_system.api.dto.response.ClassCourseSummaryResponse;
import com.course_registration_system.api.error.ErrorResponse;
import com.course_registration_system.api.mapper.ClassCourseMapper;
import com.course_registration_system.business.classcourse.ClassCourseService;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ClassCourseWithStats;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course-classes")
@Tag(name = "Course Classes", description = "Classes open for registration and class management")
public class ClassCourseController {

    private final ClassCourseService classCourseService;

    public ClassCourseController(ClassCourseService classCourseService) {
        this.classCourseService = classCourseService;
    }

    @GetMapping
    @Operation(
            summary = "List all classes",
            description = "Returns all available classes with seat counts. Permission: Public.")
    @ApiResponse(responseCode = "200", description = "List of classes")
    @ApiResponse(responseCode = "400", description = "A query parameter is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<ClassCourseSummaryResponse>> listClasses(
            @Parameter(description = "Zero-based page index")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size (1 to 100)")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        List<ClassCourseWithStats> all = classCourseService.list();
        int fromIndex = Math.min(page * size, all.size());
        int toIndex = Math.min(fromIndex + size, all.size());
        List<ClassCourseWithStats> paged = all.subList(fromIndex, toIndex);
        return ResponseEntity.ok(ClassCourseMapper.toSummaryResponseList(paged));
    }

    @GetMapping("/{classId}")
    @Operation(
            summary = "Get class details",
            description = "Returns schedule, capacity, enrolled count and available seats. Permission: Public.")
    @ApiResponse(responseCode = "200", description = "Class details")
    @ApiResponse(responseCode = "404", description = "Class not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ClassCourseDetailResponse> getClass(
            @Parameter(description = "System-generated class ID")
            @PathVariable String classId) {
        ClassCourseWithStats stats = classCourseService.get(classId);
        return ResponseEntity.ok(ClassCourseMapper.toDetailResponse(stats));
    }

    @PostMapping
    @Operation(
            summary = "Create a class",
            description = "Creates a new class with its schedule. Permission: SYSTEM_ADMIN.")
    @ApiResponse(responseCode = "201", description = "Class created")
    @ApiResponse(responseCode = "400", description = "Request body is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Missing, invalid or expired access token",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "The user is not SYSTEM_ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ClassCourseDetailResponse> createClassCourse(
            @Valid @RequestBody CreateClassCourseRequest request) {
        ClassCourse created = classCourseService.create(ClassCourseMapper.toModel(request));
        URI location = URI.create("/api/v1/course-classes/" + created.getId());
        return ResponseEntity.created(location).body(ClassCourseMapper.toDetailResponse(created, 0));
    }
}
