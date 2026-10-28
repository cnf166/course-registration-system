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
import com.course_registration_system.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// Depends on other modules' ports, not their services, to avoid circular service dependencies.
@Service
public class EnrollmentService {
    static final int CREDIT_LIMIT = 26;

    private final EnrollmentRepository enrollmentRepository;
    private final ClassCourseRepository classCourseRepository;
    private final EnrollmentPeriodRepository enrollmentPeriodRepository;
    private final Clock clock;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             ClassCourseRepository classCourseRepository,
                             EnrollmentPeriodRepository enrollmentPeriodRepository,
                             Clock clock) {
        this.enrollmentRepository = enrollmentRepository;
        this.classCourseRepository = classCourseRepository;
        this.enrollmentPeriodRepository = enrollmentPeriodRepository;
        this.clock = clock;
    }

    public List<EnrollResult> enroll(String studentId, List<String> classIds) {
        Instant now = clock.instant();

        Optional<EnrollmentPeriod> periodOpt = enrollmentPeriodRepository.findCurrent();
        boolean periodOpen = periodOpt.isPresent() && isPeriodOpen(periodOpt.get(), now);

        List<Enrollment> currentEnrollments = new ArrayList<>(
                enrollmentRepository.findByStudent(studentId));

        List<EnrollResult> results = new ArrayList<>(classIds.size());

        for (String classId : classIds) {
            EnrollResult result = attemptEnroll(studentId, classId, periodOpen, currentEnrollments, now);
            results.add(result);
            if ("ENROLLED".equals(result.status())) {
                enrollmentRepository.findByStudentAndClass(studentId, classId)
                        .ifPresent(currentEnrollments::add);
            }
        }

        return results;
    }

    private EnrollResult attemptEnroll(String studentId,
                                       String classId,
                                       boolean periodOpen,
                                       List<Enrollment> currentEnrollments,
                                       Instant now) {
        // Check 1: enrollment period open
        if (!periodOpen) {
            return EnrollResult.rejected(classId, EnrollResult.ENROLLMENT_PERIOD_CLOSED);
        }

        // Check 2: class exists
        Optional<ClassCourse> classOpt = classCourseRepository.findById(classId);
        if (classOpt.isEmpty()) {
            return EnrollResult.rejected(classId, EnrollResult.CLASS_NOT_FOUND);
        }
        ClassCourse classCourse = classOpt.get();

        // Check 3: class has seats
        // TODO: handling concurrency
        int enrolledCount = enrollmentRepository.countByClass(classId);
        if (enrolledCount >= classCourse.getCapacity()) {
            return EnrollResult.rejected(classId, EnrollResult.CLASS_FULL);
        }

        // Check 4: not already enrolled
        boolean alreadyEnrolled = currentEnrollments.stream()
                .anyMatch(e -> classId.equals(e.getClassId()));
        if (alreadyEnrolled) {
            return EnrollResult.rejected(classId, EnrollResult.ALREADY_ENROLLED);
        }

        // Check 5: no schedule conflict
        if (hasScheduleConflict(classCourse, currentEnrollments)) {
            return EnrollResult.rejected(classId, EnrollResult.SCHEDULE_CONFLICT);
        }

        // Check 6: credit limit
        int currentCredits = sumCredits(currentEnrollments);
        if (currentCredits + classCourse.getCredits() > CREDIT_LIMIT) {
            return EnrollResult.rejected(classId, EnrollResult.CREDIT_LIMIT_EXCEEDED);
        }

        // All checks passed: persist
        Enrollment enrollment = new Enrollment(
                IdUtils.generateUuid(),
                studentId,
                classId,
                now);
        enrollmentRepository.save(enrollment);

        return EnrollResult.enrolled(classId);
    }

    public void cancel(String studentId, String classId) {
        Instant now = clock.instant();
        Optional<EnrollmentPeriod> periodOpt = enrollmentPeriodRepository.findCurrent();
        if (periodOpt.isEmpty() || !isPeriodOpen(periodOpt.get(), now)) {
            throw new RuleViolationException(EnrollResult.ENROLLMENT_PERIOD_CLOSED);
        }

        Enrollment enrollment = enrollmentRepository
                .findByStudentAndClass(studentId, classId)
                .orElseThrow(() -> new NotFoundException(
                        "Student " + studentId + " is not enrolled in class " + classId));

        enrollmentRepository.delete(enrollment);
    }

    public List<Enrollment> getMyClasses(String studentId) {
        return enrollmentRepository.findByStudent(studentId);
    }


    private boolean isPeriodOpen(EnrollmentPeriod period, Instant now) {
        return !now.isBefore(period.getStartAt()) && now.isBefore(period.getEndAt());
    }

    private boolean hasScheduleConflict(ClassCourse newClass, List<Enrollment> currentEnrollments) {
        Set<String> occupiedSlots = new HashSet<>();
        for (Enrollment enrollment : currentEnrollments) {
            ClassCourse enrolled = classCourseRepository.findById(enrollment.getClassId())
                    .orElse(null);
            if (enrolled == null || enrolled.getSchedule() == null) continue;
            for (ScheduleSlot slot : enrolled.getSchedule()) {
                occupiedSlots.add(slotKey(slot));
            }
        }

        if (newClass.getSchedule() == null) return false;
        for (ScheduleSlot slot : newClass.getSchedule()) {
            if (occupiedSlots.contains(slotKey(slot))) {
                return true;
            }
        }
        return false;
    }

    private String slotKey(ScheduleSlot slot) {
        return slot.dayOfWeek() + ":" + slot.shift();
    }

    private int sumCredits(List<Enrollment> enrollments) {
        int total = 0;
        for (Enrollment enrollment : enrollments) {
            total += classCourseRepository.findById(enrollment.getClassId())
                    .map(ClassCourse::getCredits)
                    .orElse(0);
        }
        return total;
    }
}
