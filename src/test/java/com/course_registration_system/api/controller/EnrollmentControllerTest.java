package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.EnrollRequest;
import com.course_registration_system.api.error.GlobalExceptionHandler;
import com.course_registration_system.business.classcourse.ClassCourseRepository;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.enrollment.EnrollmentService;
import com.course_registration_system.business.enrollment.model.EnrollResult;
import com.course_registration_system.business.enrollment.model.Enrollment;
import com.course_registration_system.business.exception.NotFoundException;
import com.course_registration_system.business.exception.RuleViolationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    private static final String HARDCODED_STUDENT_ID = "hardcoded-student-id";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private ClassCourseRepository classCourseRepository;

    @InjectMocks
    private EnrollmentController enrollmentController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // =========================================================================
    // POST /api/v1/enrollments
    // =========================================================================

    @Test
    void enroll_validRequest_returns200OkWithBulkResponse() throws Exception {
        EnrollRequest request = new EnrollRequest(List.of("cls-1", "cls-2"));
        List<EnrollResult> results = List.of(
                new EnrollResult("cls-1", "ENROLLED", null),
                new EnrollResult("cls-2", "REJECTED", "CLASS_FULL")
        );

        when(enrollmentService.enroll(eq(HARDCODED_STUDENT_ID), eq(List.of("cls-1", "cls-2"))))
                .thenReturn(results);

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].classId", is("cls-1")))
                .andExpect(jsonPath("$.results[0].status", is("ENROLLED")))
                .andExpect(jsonPath("$.results[0].reason", nullValue()))
                .andExpect(jsonPath("$.results[1].classId", is("cls-2")))
                .andExpect(jsonPath("$.results[1].status", is("REJECTED")))
                .andExpect(jsonPath("$.results[1].reason", is("CLASS_FULL")));
    }

    @Test
    void enroll_emptyClassIds_returns400BadRequest() throws Exception {
        EnrollRequest request = new EnrollRequest(List.of());

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }

    @Test
    void enroll_blankClassId_returns400BadRequest() throws Exception {
        EnrollRequest request = new EnrollRequest(List.of("   "));

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }

    // =========================================================================
    // DELETE /api/v1/enrollments/{classId}
    // =========================================================================

    @Test
    void cancelEnrollment_validClass_returns204NoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/enrollments/cls-1"))
                .andExpect(status().isNoContent());

        verify(enrollmentService).cancel(HARDCODED_STUDENT_ID, "cls-1");
    }

    @Test
    void cancelEnrollment_notEnrolled_returns404NotFound() throws Exception {
        doThrow(new NotFoundException("Enrollment not found for student " + HARDCODED_STUDENT_ID))
                .when(enrollmentService).cancel(HARDCODED_STUDENT_ID, "cls-missing");

        mockMvc.perform(delete("/api/v1/enrollments/cls-missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Enrollment not found for student " + HARDCODED_STUDENT_ID)));
    }

    @Test
    void cancelEnrollment_periodClosed_returns409Conflict() throws Exception {
        doThrow(new RuleViolationException("ENROLLMENT_PERIOD_CLOSED"))
                .when(enrollmentService).cancel(HARDCODED_STUDENT_ID, "cls-1");

        mockMvc.perform(delete("/api/v1/enrollments/cls-1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("ENROLLMENT_PERIOD_CLOSED")));
    }

    // =========================================================================
    // GET /api/v1/enrollments/my-classes
    // =========================================================================

    @Test
    void getMyClasses_hasEnrolledClasses_returns200WithItemsAndTotalCredits() throws Exception {
        Enrollment e1 = new Enrollment("e-1", HARDCODED_STUDENT_ID, "cls-101", Instant.EPOCH);
        Enrollment e2 = new Enrollment("e-2", HARDCODED_STUDENT_ID, "cls-102", Instant.EPOCH);

        ClassCourse c1 = new ClassCourse("cls-101", "Operating Systems", 3, 50, List.of(), Instant.EPOCH);
        ClassCourse c2 = new ClassCourse("cls-102", "Database Systems", 4, 60, List.of(), Instant.EPOCH);

        when(enrollmentService.getMyClasses(HARDCODED_STUDENT_ID)).thenReturn(List.of(e1, e2));
        when(classCourseRepository.findByIds(List.of("cls-101", "cls-102"))).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/v1/enrollments/my-classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].classId", is("cls-101")))
                .andExpect(jsonPath("$.items[0].name", is("Operating Systems")))
                .andExpect(jsonPath("$.items[0].credits", is(3)))
                .andExpect(jsonPath("$.items[0].status", is("ENROLLED")))
                .andExpect(jsonPath("$.items[1].classId", is("cls-102")))
                .andExpect(jsonPath("$.items[1].name", is("Database Systems")))
                .andExpect(jsonPath("$.items[1].credits", is(4)))
                .andExpect(jsonPath("$.totalCredits", is(7)));
    }

    @Test
    void getMyClasses_noEnrolledClasses_returns200WithEmptyItemsAndZeroCredits() throws Exception {
        when(enrollmentService.getMyClasses(HARDCODED_STUDENT_ID)).thenReturn(List.of());
        when(classCourseRepository.findByIds(List.of())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/enrollments/my-classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.totalCredits", is(0)));
    }
}
