package com.course_registration_system.business.classcourse;

import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ClassCourseWithStats;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;
import com.course_registration_system.business.enrollment.EnrollmentRepository;
import com.course_registration_system.business.exception.InvalidInputException;
import com.course_registration_system.business.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassCourseServiceTest {

    static final String CLASS_A = "class-A";
    static final String CLASS_B = "class-B";

    static final Instant FIXED_NOW = Instant.parse("2026-11-02T05:00:00Z");

    static ClassCourse newClass(String name, int credits, int capacity, List<ScheduleSlot> schedule) {
        return new ClassCourse(null, name, credits, capacity, schedule, null);
    }

    static ClassCourse storedClass(String id, int credits, int capacity) {
        return new ClassCourse(id, "Class " + id, credits, capacity,
                List.of(new ScheduleSlot(2, 1)), Instant.EPOCH);
    }

    @Mock ClassCourseRepository classCourseRepository;
    @Mock EnrollmentRepository enrollmentRepository;

    ClassCourseService service;

    @BeforeEach
    void setUp() {
        Clock fixed = Clock.fixed(FIXED_NOW, ZoneOffset.UTC);
        service = new ClassCourseService(classCourseRepository, enrollmentRepository, fixed);
    }

    // =========================================================================
    // create
    // =========================================================================

    @Test
    void create_happyPath_assignsIdAndCreatedAtThenSaves() {
        when(classCourseRepository.save(any(ClassCourse.class))).thenAnswer(inv -> inv.getArgument(0));

        ClassCourse result = service.create(newClass("Database - Group 01", 3, 60,
                List.of(new ScheduleSlot(2, 1), new ScheduleSlot(4, 1))));

        ArgumentCaptor<ClassCourse> saved = ArgumentCaptor.forClass(ClassCourse.class);
        verify(classCourseRepository).save(saved.capture());
        assertThat(saved.getValue().getId()).matches("[0-9a-f]{32}");
        assertThat(saved.getValue().getCreatedAt()).isEqualTo(FIXED_NOW);
        assertThat(saved.getValue().getName()).isEqualTo("Database - Group 01");
        assertThat(saved.getValue().getCredits()).isEqualTo(3);
        assertThat(saved.getValue().getCapacity()).isEqualTo(60);
        assertThat(saved.getValue().getSchedule())
                .containsExactly(new ScheduleSlot(2, 1), new ScheduleSlot(4, 1));
        assertThat(result).isSameAs(saved.getValue());
    }

    @Test
    void create_ignoresIdAndCreatedAtSuppliedByCaller() {
        when(classCourseRepository.save(any(ClassCourse.class))).thenAnswer(inv -> inv.getArgument(0));
        ClassCourse input = new ClassCourse("client-chosen-id", "Algorithms", 3, 40,
                List.of(new ScheduleSlot(3, 2)), Instant.EPOCH);

        ClassCourse result = service.create(input);

        assertThat(result.getId()).isNotEqualTo("client-chosen-id");
        assertThat(result.getCreatedAt()).isEqualTo(FIXED_NOW);
        assertThat(input.getId()).isEqualTo("client-chosen-id"); // the argument is not mutated
    }

    @Test
    void create_trimsName() {
        when(classCourseRepository.save(any(ClassCourse.class))).thenAnswer(inv -> inv.getArgument(0));

        ClassCourse result = service.create(newClass("  Algorithms  ", 3, 40, List.of(new ScheduleSlot(3, 2))));

        assertThat(result.getName()).isEqualTo("Algorithms");
    }

    @Test
    void create_duplicateSlots_areSavedOnce() {
        when(classCourseRepository.save(any(ClassCourse.class))).thenAnswer(inv -> inv.getArgument(0));

        ClassCourse result = service.create(newClass("Algorithms", 3, 40,
                List.of(new ScheduleSlot(3, 2), new ScheduleSlot(3, 2), new ScheduleSlot(5, 2))));

        assertThat(result.getSchedule()).containsExactly(new ScheduleSlot(3, 2), new ScheduleSlot(5, 2));
    }

    @Test
    void create_nullClass_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(null)).isInstanceOf(InvalidInputException.class);
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_blankName_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("   ", 3, 40, List.of(new ScheduleSlot(3, 2)))))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("name");
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_zeroCredits_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 0, 40, List.of(new ScheduleSlot(3, 2)))))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Credits");
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_negativeCredits_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", -1, 40, List.of(new ScheduleSlot(3, 2)))))
                .isInstanceOf(InvalidInputException.class);
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_zeroCapacity_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 0, List.of(new ScheduleSlot(3, 2)))))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Capacity");
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_emptySchedule_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 40, List.of())))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("Schedule");
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_nullSchedule_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 40, null)))
                .isInstanceOf(InvalidInputException.class);
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_slotWithDayOutOfRange_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 40, List.of(new ScheduleSlot(8, 1)))))
                .isInstanceOf(InvalidInputException.class);
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 40, List.of(new ScheduleSlot(0, 1)))))
                .isInstanceOf(InvalidInputException.class);
        verify(classCourseRepository, never()).save(any());
    }

    @Test
    void create_slotWithShiftOutOfRange_throwsInvalidInput() {
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 40, List.of(new ScheduleSlot(2, 5)))))
                .isInstanceOf(InvalidInputException.class);
        assertThatThrownBy(() -> service.create(newClass("Algorithms", 3, 40, List.of(new ScheduleSlot(2, 0)))))
                .isInstanceOf(InvalidInputException.class);
        verify(classCourseRepository, never()).save(any());
    }

    // =========================================================================
    // get
    // =========================================================================

    @Test
    void get_existingClass_returnsClassWithEnrolledCountAndAvailableSeats() {
        ClassCourse cls = storedClass(CLASS_A, 3, 60);
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(cls));
        when(enrollmentRepository.countByClass(CLASS_A)).thenReturn(42);

        ClassCourseWithStats result = service.get(CLASS_A);

        assertThat(result.classCourse()).isSameAs(cls);
        assertThat(result.enrolledCount()).isEqualTo(42);
        assertThat(result.availableSeats()).isEqualTo(18);
    }

    @Test
    void get_unknownClass_throwsNotFound() {
        when(classCourseRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get("missing"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("missing");
        verify(enrollmentRepository, never()).countByClass(any());
    }

    // =========================================================================
    // list
    // =========================================================================

    @Test
    void list_returnsEveryClassWithItsOwnEnrolledCount() {
        ClassCourse a = storedClass(CLASS_A, 3, 60);
        ClassCourse b = storedClass(CLASS_B, 2, 30);
        when(classCourseRepository.findAll()).thenReturn(List.of(a, b));
        when(enrollmentRepository.countByClass(CLASS_A)).thenReturn(10);
        when(enrollmentRepository.countByClass(CLASS_B)).thenReturn(30);

        List<ClassCourseWithStats> result = service.list();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).classCourse()).isSameAs(a);
        assertThat(result.get(0).enrolledCount()).isEqualTo(10);
        assertThat(result.get(0).availableSeats()).isEqualTo(50);
        assertThat(result.get(1).classCourse()).isSameAs(b);
        assertThat(result.get(1).enrolledCount()).isEqualTo(30);
        assertThat(result.get(1).availableSeats()).isZero();
    }

    @Test
    void list_noClasses_returnsEmptyList() {
        when(classCourseRepository.findAll()).thenReturn(List.of());

        assertThat(service.list()).isEmpty();
        verify(enrollmentRepository, never()).countByClass(any());
    }

    // =========================================================================
    // ClassCourseWithStats
    // =========================================================================

    @Test
    void availableSeats_neverNegative() {
        ClassCourseWithStats overbooked = new ClassCourseWithStats(storedClass(CLASS_A, 3, 10), 12);

        assertThat(overbooked.availableSeats()).isZero();
    }
}
