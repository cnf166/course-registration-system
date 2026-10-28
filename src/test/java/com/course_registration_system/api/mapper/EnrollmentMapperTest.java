package com.course_registration_system.api.mapper;

import com.course_registration_system.api.dto.response.BulkEnrollResponse;
import com.course_registration_system.api.dto.response.MyClassesResponse;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.enrollment.model.EnrollResult;
import com.course_registration_system.business.enrollment.model.Enrollment;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentMapperTest {

    @Test
    void toBulkResponse_mapsResultsToItems() {
        List<EnrollResult> results = List.of(
                new EnrollResult("cls-1", "ENROLLED", null),
                new EnrollResult("cls-2", "REJECTED", "CLASS_FULL")
        );

        BulkEnrollResponse response = EnrollmentMapper.toBulkResponse(results);

        assertThat(response).isNotNull();
        assertThat(response.results()).hasSize(2);
        assertThat(response.results().get(0).classId()).isEqualTo("cls-1");
        assertThat(response.results().get(0).status()).isEqualTo("ENROLLED");
        assertThat(response.results().get(0).reason()).isNull();
        assertThat(response.results().get(1).classId()).isEqualTo("cls-2");
        assertThat(response.results().get(1).status()).isEqualTo("REJECTED");
        assertThat(response.results().get(1).reason()).isEqualTo("CLASS_FULL");
    }

    @Test
    void toBulkResponse_emptyList_returnsEmptyResults() {
        BulkEnrollResponse response = EnrollmentMapper.toBulkResponse(List.of());

        assertThat(response).isNotNull();
        assertThat(response.results()).isEmpty();
    }

    @Test
    void toMyClassesResponse_mapsEnrollmentsAndCalculatesTotalCredits() {
        Enrollment e1 = new Enrollment("e-1", "student-1", "cls-1", Instant.EPOCH);
        Enrollment e2 = new Enrollment("e-2", "student-1", "cls-2", Instant.EPOCH);

        ClassCourse c1 = new ClassCourse("cls-1", "Database Systems", 3, 50, List.of(), Instant.EPOCH);
        ClassCourse c2 = new ClassCourse("cls-2", "Computer Networks", 4, 60, List.of(), Instant.EPOCH);

        Map<String, ClassCourse> classById = Map.of(
                "cls-1", c1,
                "cls-2", c2
        );

        MyClassesResponse response = EnrollmentMapper.toMyClassesResponse(List.of(e1, e2), classById);

        assertThat(response).isNotNull();
        assertThat(response.items()).hasSize(2);
        assertThat(response.items().get(0).classId()).isEqualTo("cls-1");
        assertThat(response.items().get(0).name()).isEqualTo("Database Systems");
        assertThat(response.items().get(0).credits()).isEqualTo(3);
        assertThat(response.items().get(0).status()).isEqualTo("ENROLLED");
        assertThat(response.items().get(1).classId()).isEqualTo("cls-2");
        assertThat(response.items().get(1).name()).isEqualTo("Computer Networks");
        assertThat(response.items().get(1).credits()).isEqualTo(4);
        assertThat(response.totalCredits()).isEqualTo(7);
    }

    @Test
    void toMyClassesResponse_missingClassInMap_isFilteredOut() {
        Enrollment e1 = new Enrollment("e-1", "student-1", "cls-1", Instant.EPOCH);
        Enrollment e2 = new Enrollment("e-2", "student-1", "cls-unknown", Instant.EPOCH);

        ClassCourse c1 = new ClassCourse("cls-1", "Database Systems", 3, 50, List.of(), Instant.EPOCH);
        Map<String, ClassCourse> classById = Map.of("cls-1", c1);

        MyClassesResponse response = EnrollmentMapper.toMyClassesResponse(List.of(e1, e2), classById);

        assertThat(response).isNotNull();
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).classId()).isEqualTo("cls-1");
        assertThat(response.totalCredits()).isEqualTo(3);
    }

    @Test
    void toMyClassesResponse_emptyEnrollments_returnsEmptyItemsAndZeroCredits() {
        MyClassesResponse response = EnrollmentMapper.toMyClassesResponse(List.of(), Map.of());

        assertThat(response).isNotNull();
        assertThat(response.items()).isEmpty();
        assertThat(response.totalCredits()).isZero();
    }
}
