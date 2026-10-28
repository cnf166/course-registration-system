package com.course_registration_system.business.classcourse;

import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ClassCourseWithStats;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;
import com.course_registration_system.business.enrollment.EnrollmentRepository;
import com.course_registration_system.business.exception.InvalidInputException;
import com.course_registration_system.business.exception.NotFoundException;
import com.course_registration_system.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ClassCourseService {
	private static final int MIN_DAY = 1;
	private static final int MAX_DAY = 7;
	private static final int MIN_SHIFT = 1;
	private static final int MAX_SHIFT = 4;

	private final ClassCourseRepository classCourseRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final Clock clock;

	public ClassCourseService(ClassCourseRepository classCourseRepository,
							  EnrollmentRepository enrollmentRepository,
							  Clock clock) {
		this.classCourseRepository = classCourseRepository;
		this.enrollmentRepository = enrollmentRepository;
		this.clock = clock;
	}

	public ClassCourseWithStats get(String classId) {
		ClassCourse classCourse = classCourseRepository.findById(classId)
				.orElseThrow(() -> new NotFoundException("Class not found: " + classId));
		return withStats(classCourse);
	}

	public List<ClassCourseWithStats> list() {
		return classCourseRepository.findAll().stream()
				.map(this::withStats)
				.toList();
	}

	public ClassCourse create(ClassCourse classCourse) {
		validate(classCourse);

		ClassCourse toSave = new ClassCourse(
				IdUtils.generateUuid(),
				classCourse.getName().trim(),
				classCourse.getCredits(),
				classCourse.getCapacity(),
				List.copyOf(new LinkedHashSet<>(classCourse.getSchedule())),
				clock.instant());

		return classCourseRepository.save(toSave);
	}

	private ClassCourseWithStats withStats(ClassCourse classCourse) {
		return new ClassCourseWithStats(classCourse, enrollmentRepository.countByClass(classCourse.getId()));
	}

	private void validate(ClassCourse classCourse) {
		if (classCourse == null) {
			throw new InvalidInputException("Class must not be null");
		}
		if (classCourse.getName() == null || classCourse.getName().isBlank()) {
			throw new InvalidInputException("Class name must not be blank");
		}
		if (classCourse.getCredits() <= 0) {
			throw new InvalidInputException("Credits must be greater than 0");
		}
		if (classCourse.getCapacity() <= 0) {
			throw new InvalidInputException("Capacity must be greater than 0");
		}
		List<ScheduleSlot> schedule = classCourse.getSchedule();
		if (schedule == null || schedule.isEmpty()) {
			throw new InvalidInputException("Schedule must not be empty");
		}
		for (ScheduleSlot slot : schedule) {
			if (slot == null
					|| slot.dayOfWeek() < MIN_DAY || slot.dayOfWeek() > MAX_DAY
					|| slot.shift() < MIN_SHIFT || slot.shift() > MAX_SHIFT) {
				throw new InvalidInputException("Schedule slot is invalid: dayOfWeek must be "
						+ MIN_DAY + "-" + MAX_DAY + " and shift must be " + MIN_SHIFT + "-" + MAX_SHIFT);
			}
		}
	}
}