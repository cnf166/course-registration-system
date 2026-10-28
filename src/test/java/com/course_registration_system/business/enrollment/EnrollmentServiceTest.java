package com.course_registration_system.business.enrollment;

import com.course_registration_system.business.classcourse.ClassCourseRepository;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;
import com.course_registration_system.business.enrollment.model.EnrollResult;
import com.course_registration_system.business.enrollment.model.Enrollment;
import com.course_registration_system.business.enrollmentperiod.EnrollmentPeriodRepository;
import com.course_registration_system.business.enrollmentperiod.model.EnrollmentPeriod;
import com.course_registration_system.business.exception.NotFoundException;
import com.course_registration_system.business.exception.RuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class EnrollmentServiceTest {

    static final String STUDENT_ID = "student-001";
    static final String CLASS_A    = "class-A";
    static final String CLASS_B    = "class-B";

    static final Instant FIXED_NOW    = Instant.parse("2026-11-02T05:00:00Z");
    static final Instant PERIOD_START = Instant.parse("2026-11-01T01:00:00Z");
    static final Instant PERIOD_END   = Instant.parse("2026-11-04T10:00:00Z");

    static final EnrollmentPeriod OPEN_PERIOD =
            new EnrollmentPeriod(PERIOD_START, PERIOD_END, PERIOD_START);

    static final EnrollmentPeriod CLOSED_PERIOD =
            new EnrollmentPeriod(PERIOD_START, PERIOD_START, PERIOD_START);

    static ClassCourse classWithSlot(String id, int credits, int capacity, int dayOfWeek, int shift) {
        return new ClassCourse(id, "Class " + id, credits, capacity,
                List.of(new ScheduleSlot(dayOfWeek, shift)), Instant.EPOCH);
    }

    // ---------------------------------------------------------------------------
    // Dependencies
    // ---------------------------------------------------------------------------

    @Mock EnrollmentRepository enrollmentRepository;
    @Mock ClassCourseRepository classCourseRepository;
    @Mock EnrollmentPeriodRepository enrollmentPeriodRepository;

    EnrollmentService service;

    @BeforeEach
    void setUp() {
        Clock fixed = Clock.fixed(FIXED_NOW, ZoneOffset.UTC);
        service = new EnrollmentService(
                enrollmentRepository, classCourseRepository, enrollmentPeriodRepository, fixed);
    }

    // =========================================================================
    // enroll - happy path
    // =========================================================================

    @Test
    void enroll_happyPath_returnsEnrolled() {
        ClassCourse cls = classWithSlot(CLASS_A, 3, 60, 2, 1);
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(cls));
        when(enrollmentRepository.countByClass(CLASS_A)).thenReturn(0);
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(enrollmentRepository.findByStudentAndClass(STUDENT_ID, CLASS_A))
                .thenReturn(Optional.of(new Enrollment("id", STUDENT_ID, CLASS_A, FIXED_NOW)));

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).status()).isEqualTo("ENROLLED");
        assertThat(results.get(0).reason()).isNull();
        verify(enrollmentRepository).save(any());
    }

    // =========================================================================
    // enroll - check 1: ENROLLMENT_PERIOD_CLOSED
    // =========================================================================

    @Test
    void enroll_noPeriod_rejectsAllWithPeriodClosed() {
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.empty());
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A, CLASS_B));

        assertThat(results).allSatisfy(r -> {
            assertThat(r.status()).isEqualTo("REJECTED");
            assertThat(r.reason()).isEqualTo(EnrollResult.ENROLLMENT_PERIOD_CLOSED);
        });
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enroll_periodExpired_rejectsWithPeriodClosed() {
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(CLOSED_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A));

        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.ENROLLMENT_PERIOD_CLOSED);
    }

    // =========================================================================
    // enroll - check 2: CLASS_NOT_FOUND
    // =========================================================================

    @Test
    void enroll_classNotFound_rejectsWithClassNotFound() {
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.empty());

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A));

        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.CLASS_NOT_FOUND);
        verify(enrollmentRepository, never()).save(any());
    }

    // =========================================================================
    // enroll - check 3: CLASS_FULL
    // =========================================================================

    @Test
    void enroll_classFull_rejectsWithClassFull() {
        ClassCourse cls = classWithSlot(CLASS_A, 3, 2, 2, 1);
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(cls));
        when(enrollmentRepository.countByClass(CLASS_A)).thenReturn(2); // capacity == 2, full

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A));

        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.CLASS_FULL);
        verify(enrollmentRepository, never()).save(any());
    }

    // =========================================================================
    // enroll - check 4: ALREADY_ENROLLED
    // =========================================================================

    @Test
    void enroll_alreadyEnrolled_rejectsWithAlreadyEnrolled() {
        ClassCourse cls = classWithSlot(CLASS_A, 3, 60, 2, 1);
        Enrollment existing = new Enrollment("existing-id", STUDENT_ID, CLASS_A, FIXED_NOW);

        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of(existing));
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(cls));
        when(enrollmentRepository.countByClass(CLASS_A)).thenReturn(1);
        // Need classCourse for credit sum and conflict check of existing enrollment
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(cls));

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A));

        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.ALREADY_ENROLLED);
        verify(enrollmentRepository, never()).save(any());
    }

    // =========================================================================
    // enroll - check 5: SCHEDULE_CONFLICT
    // =========================================================================

    @Test
    void enroll_scheduleConflict_rejectsWithScheduleConflict() {
        // Student already enrolled in CLASS_A on Monday shift 1
        ClassCourse clsA = classWithSlot(CLASS_A, 3, 60, 2, 1);
        Enrollment existingA = new Enrollment("enr-A", STUDENT_ID, CLASS_A, FIXED_NOW);

        // CLASS_B also on Monday shift 1 - conflict
        ClassCourse clsB = classWithSlot(CLASS_B, 3, 60, 2, 1);

        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of(existingA));
        when(classCourseRepository.findById(CLASS_B)).thenReturn(Optional.of(clsB));
        when(enrollmentRepository.countByClass(CLASS_B)).thenReturn(0);
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(clsA)); // for conflict check

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_B));

        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.SCHEDULE_CONFLICT);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enroll_differentSlot_noConflict() {
        // Student enrolled in CLASS_A on Monday shift 1
        ClassCourse clsA = classWithSlot(CLASS_A, 3, 60, 2, 1);
        Enrollment existingA = new Enrollment("enr-A", STUDENT_ID, CLASS_A, FIXED_NOW);

        // CLASS_B on Wednesday shift 2 - no conflict
        ClassCourse clsB = classWithSlot(CLASS_B, 3, 60, 4, 2);

        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of(existingA));
        when(classCourseRepository.findById(CLASS_B)).thenReturn(Optional.of(clsB));
        when(enrollmentRepository.countByClass(CLASS_B)).thenReturn(0);
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(clsA));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(enrollmentRepository.findByStudentAndClass(STUDENT_ID, CLASS_B))
                .thenReturn(Optional.of(new Enrollment("id", STUDENT_ID, CLASS_B, FIXED_NOW)));

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_B));

        assertThat(results.get(0).status()).isEqualTo("ENROLLED");
    }

    // =========================================================================
    // enroll - check 6: CREDIT_LIMIT_EXCEEDED
    // =========================================================================

    @Test
    void enroll_creditLimitExceeded_rejectsWithCreditLimitExceeded() {
        // Student already enrolled in a 24-credit class
        ClassCourse existing = classWithSlot(CLASS_A, 24, 60, 2, 1);
        Enrollment existingEnr = new Enrollment("enr-A", STUDENT_ID, CLASS_A, FIXED_NOW);

        // New class would add 3 credits → 27 > 26
        ClassCourse newCls = classWithSlot(CLASS_B, 3, 60, 4, 2);

        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of(existingEnr));
        when(classCourseRepository.findById(CLASS_B)).thenReturn(Optional.of(newCls));
        when(enrollmentRepository.countByClass(CLASS_B)).thenReturn(0);
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(existing));

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_B));

        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.CREDIT_LIMIT_EXCEEDED);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enroll_exactlyAtCreditLimit_isAllowed() {
        // 23 credits already enrolled, adding 3 = 26 = limit → should succeed
        ClassCourse existing = classWithSlot(CLASS_A, 23, 60, 2, 1);
        Enrollment existingEnr = new Enrollment("enr-A", STUDENT_ID, CLASS_A, FIXED_NOW);

        ClassCourse newCls = classWithSlot(CLASS_B, 3, 60, 4, 2);

        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of(existingEnr));
        when(classCourseRepository.findById(CLASS_B)).thenReturn(Optional.of(newCls));
        when(enrollmentRepository.countByClass(CLASS_B)).thenReturn(0);
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(enrollmentRepository.findByStudentAndClass(STUDENT_ID, CLASS_B))
                .thenReturn(Optional.of(new Enrollment("id", STUDENT_ID, CLASS_B, FIXED_NOW)));

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_B));

        assertThat(results.get(0).status()).isEqualTo("ENROLLED");
    }

    // =========================================================================
    // enroll - bulk independence
    // =========================================================================

    @Test
    void enroll_oneRejectedOneEnrolled_bothReturnedIndependently() {
        ClassCourse clsA = classWithSlot(CLASS_A, 3, 0, 2, 1); // capacity 0 → full
        ClassCourse clsB = classWithSlot(CLASS_B, 3, 60, 4, 2);

        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());
        when(classCourseRepository.findById(CLASS_A)).thenReturn(Optional.of(clsA));
        when(enrollmentRepository.countByClass(CLASS_A)).thenReturn(0);
        when(classCourseRepository.findById(CLASS_B)).thenReturn(Optional.of(clsB));
        when(enrollmentRepository.countByClass(CLASS_B)).thenReturn(0);
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(enrollmentRepository.findByStudentAndClass(STUDENT_ID, CLASS_B))
                .thenReturn(Optional.of(new Enrollment("id", STUDENT_ID, CLASS_B, FIXED_NOW)));

        List<EnrollResult> results = service.enroll(STUDENT_ID, List.of(CLASS_A, CLASS_B));

        assertThat(results).hasSize(2);
        assertThat(results.get(0).reason()).isEqualTo(EnrollResult.CLASS_FULL);
        assertThat(results.get(1).status()).isEqualTo("ENROLLED");
    }

    // =========================================================================
    // cancel - happy path
    // =========================================================================

    @Test
    void cancel_happyPath_deletesEnrollment() {
        Enrollment enrollment = new Enrollment("enr-id", STUDENT_ID, CLASS_A, FIXED_NOW);
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudentAndClass(STUDENT_ID, CLASS_A))
                .thenReturn(Optional.of(enrollment));

        service.cancel(STUDENT_ID, CLASS_A);

        verify(enrollmentRepository).delete(enrollment);
    }

    // =========================================================================
    // cancel - period closed
    // =========================================================================

    @Test
    void cancel_periodClosed_throwsRuleViolation() {
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(CLOSED_PERIOD));

        assertThatThrownBy(() -> service.cancel(STUDENT_ID, CLASS_A))
                .isInstanceOf(RuleViolationException.class);
        verify(enrollmentRepository, never()).delete(any());
    }

    @Test
    void cancel_noPeriod_throwsRuleViolation() {
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(STUDENT_ID, CLASS_A))
                .isInstanceOf(RuleViolationException.class);
    }

    // =========================================================================
    // cancel - not enrolled
    // =========================================================================

    @Test
    void cancel_notEnrolled_throwsNotFoundException() {
        when(enrollmentPeriodRepository.findCurrent()).thenReturn(Optional.of(OPEN_PERIOD));
        when(enrollmentRepository.findByStudentAndClass(STUDENT_ID, CLASS_A))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(STUDENT_ID, CLASS_A))
                .isInstanceOf(NotFoundException.class);
        verify(enrollmentRepository, never()).delete(any());
    }

    // =========================================================================
    // getMyClasses
    // =========================================================================

    @Test
    void getMyClasses_returnsAllStudentEnrollments() {
        Enrollment e1 = new Enrollment("e1", STUDENT_ID, CLASS_A, FIXED_NOW);
        Enrollment e2 = new Enrollment("e2", STUDENT_ID, CLASS_B, FIXED_NOW);
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of(e1, e2));

        List<Enrollment> result = service.getMyClasses(STUDENT_ID);

        assertThat(result).containsExactly(e1, e2);
    }

    @Test
    void getMyClasses_noEnrollments_returnsEmptyList() {
        when(enrollmentRepository.findByStudent(STUDENT_ID)).thenReturn(List.of());

        assertThat(service.getMyClasses(STUDENT_ID)).isEmpty();
    }
}
