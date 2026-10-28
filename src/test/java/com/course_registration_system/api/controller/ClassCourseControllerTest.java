package com.course_registration_system.api.controller;

import com.course_registration_system.api.dto.request.CreateClassCourseRequest;
import com.course_registration_system.api.dto.response.ScheduleSlotDto;
import com.course_registration_system.api.error.GlobalExceptionHandler;
import com.course_registration_system.business.classcourse.ClassCourseService;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ClassCourseWithStats;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;
import com.course_registration_system.business.exception.NotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClassCourseControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ClassCourseService classCourseService;

    @InjectMocks
    private ClassCourseController classCourseController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(classCourseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // =========================================================================
    // GET /api/v1/course-classes
    // =========================================================================

    @Test
    void listClasses_returnsOkWithPaginatedSummaries() throws Exception {
        ClassCourse c1 = new ClassCourse("cls-1", "Class 1", 3, 50,
                List.of(new ScheduleSlot(2, 1)), Instant.EPOCH);
        ClassCourse c2 = new ClassCourse("cls-2", "Class 2", 3, 60,
                List.of(new ScheduleSlot(3, 2)), Instant.EPOCH);

        when(classCourseService.list()).thenReturn(List.of(
                new ClassCourseWithStats(c1, 15),
                new ClassCourseWithStats(c2, 60)
        ));

        mockMvc.perform(get("/api/v1/course-classes")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].classId", is("cls-1")))
                .andExpect(jsonPath("$[0].name", is("Class 1")))
                .andExpect(jsonPath("$[0].enrolledCount", is(15)))
                .andExpect(jsonPath("$[0].availableSeats", is(35)))
                .andExpect(jsonPath("$[1].classId", is("cls-2")))
                .andExpect(jsonPath("$[1].availableSeats", is(0)));
    }

    @Test
    void listClasses_paginationSubList_worksCorrectly() throws Exception {
        ClassCourse c1 = new ClassCourse("cls-1", "Class 1", 3, 50, List.of(), Instant.EPOCH);
        ClassCourse c2 = new ClassCourse("cls-2", "Class 2", 3, 50, List.of(), Instant.EPOCH);
        ClassCourse c3 = new ClassCourse("cls-3", "Class 3", 3, 50, List.of(), Instant.EPOCH);

        when(classCourseService.list()).thenReturn(List.of(
                new ClassCourseWithStats(c1, 0),
                new ClassCourseWithStats(c2, 0),
                new ClassCourseWithStats(c3, 0)
        ));

        // Page 1, size 2 should return only c3
        mockMvc.perform(get("/api/v1/course-classes")
                        .param("page", "1")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].classId", is("cls-3")));
    }

    // =========================================================================
    // GET /api/v1/course-classes/{classId}
    // =========================================================================

    @Test
    void getClass_existingClass_returnsDetail() throws Exception {
        ClassCourse cls = new ClassCourse("cls-101", "Database Systems", 3, 60,
                List.of(new ScheduleSlot(2, 1)), Instant.EPOCH);
        ClassCourseWithStats stats = new ClassCourseWithStats(cls, 42);

        when(classCourseService.get("cls-101")).thenReturn(stats);

        mockMvc.perform(get("/api/v1/course-classes/cls-101")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId", is("cls-101")))
                .andExpect(jsonPath("$.name", is("Database Systems")))
                .andExpect(jsonPath("$.credits", is(3)))
                .andExpect(jsonPath("$.capacity", is(60)))
                .andExpect(jsonPath("$.enrolledCount", is(42)))
                .andExpect(jsonPath("$.availableSeats", is(18)))
                .andExpect(jsonPath("$.schedule", hasSize(1)))
                .andExpect(jsonPath("$.schedule[0].dayOfWeek", is(2)))
                .andExpect(jsonPath("$.schedule[0].shift", is(1)));
    }

    @Test
    void getClass_unknownClass_returnsNotFound() throws Exception {
        when(classCourseService.get("cls-999"))
                .thenThrow(new NotFoundException("Class not found: cls-999"));

        mockMvc.perform(get("/api/v1/course-classes/cls-999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Class not found: cls-999")));
    }

    // =========================================================================
    // POST /api/v1/course-classes
    // =========================================================================

    @Test
    void createClassCourse_validRequest_returnsCreatedWithLocation() throws Exception {
        CreateClassCourseRequest request = new CreateClassCourseRequest(
                "Software Architecture",
                3,
                60,
                List.of(new ScheduleSlotDto(2, 1), new ScheduleSlotDto(4, 1))
        );

        ClassCourse created = new ClassCourse(
                "cls-new-uuid",
                "Software Architecture",
                3,
                60,
                List.of(new ScheduleSlot(2, 1), new ScheduleSlot(4, 1)),
                Instant.EPOCH
        );

        when(classCourseService.create(any(ClassCourse.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/course-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/course-classes/cls-new-uuid"))
                .andExpect(jsonPath("$.classId", is("cls-new-uuid")))
                .andExpect(jsonPath("$.name", is("Software Architecture")))
                .andExpect(jsonPath("$.credits", is(3)))
                .andExpect(jsonPath("$.capacity", is(60)))
                .andExpect(jsonPath("$.enrolledCount", is(0)))
                .andExpect(jsonPath("$.availableSeats", is(60)))
                .andExpect(jsonPath("$.schedule", hasSize(2)));
    }

    @Test
    void createClassCourse_invalidBody_blankName_returnsBadRequest() throws Exception {
        CreateClassCourseRequest request = new CreateClassCourseRequest(
                "",
                3,
                60,
                List.of(new ScheduleSlotDto(2, 1))
        );

        mockMvc.perform(post("/api/v1/course-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }

    @Test
    void createClassCourse_invalidBody_emptySchedule_returnsBadRequest() throws Exception {
        CreateClassCourseRequest request = new CreateClassCourseRequest(
                "Algorithms",
                3,
                60,
                List.of()
        );

        mockMvc.perform(post("/api/v1/course-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("VALIDATION_ERROR")));
    }
}
