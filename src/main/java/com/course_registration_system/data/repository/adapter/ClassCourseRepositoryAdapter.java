package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.classcourse.ClassCourseRepository;
import com.course_registration_system.business.classcourse.model.ClassCourse;
import com.course_registration_system.business.classcourse.model.ScheduleSlot;
import com.course_registration_system.data.entity.ClassCourseEntity;
import com.course_registration_system.data.entity.ClassScheduleEntity;
import org.springframework.stereotype.Repository;
import org.thingai.base.dao.Dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.course_registration_system.utils.TimeUtils.fromDbString;
import static com.course_registration_system.utils.TimeUtils.toDbString;

@Repository
public class ClassCourseRepositoryAdapter implements ClassCourseRepository {
	private final Dao dao;

	public ClassCourseRepositoryAdapter(Dao dao) {
		this.dao = dao;
	}

	@Override
	public Optional<ClassCourse> findById(String id) {
		ClassCourseEntity[] rows = dao.query(ClassCourseEntity.class, "id", id);
		if (rows.length == 0) {
			return Optional.empty();
		}
		ClassScheduleEntity[] slots = dao.query(ClassScheduleEntity.class, "class_id", id);
		return Optional.of(toBusinessModel(rows[0], Arrays.asList(slots)));
	}

	@Override
	public List<ClassCourse> findAll() {
		// load both tables once and join in memory, instead of one schedule query per class
		Map<String, List<ClassScheduleEntity>> slotsByClass = Arrays.stream(dao.readAll(ClassScheduleEntity.class))
				.collect(Collectors.groupingBy(ClassScheduleEntity::getClassId));

		return Arrays.stream(dao.readAll(ClassCourseEntity.class))
				.map(entity -> toBusinessModel(entity, slotsByClass.getOrDefault(entity.getId(), List.of())))
				.toList();
	}

	@Override
	public List<ClassCourse> findByIds(Collection<String> ids) {
		// one lookup per id: the Dao has no parameterized IN query, and raw SQL would allow injection
		List<ClassCourse> result = new ArrayList<>();
		for (String id : ids) {
			findById(id).ifPresent(result::add);
		}
		return result;
	}

	@Override
	public boolean existsById(String id) {
		return dao.query(ClassCourseEntity.class, "id", id).length > 0;
	}

	@Override
	public ClassCourse save(ClassCourse classCourse) {
		ClassCourseEntity entity = toEntity(classCourse);
		dao.insertOrUpdate(entity);

		// replace the whole schedule, so slots removed from the model don't stay in the table
		dao.deleteByColumn(ClassScheduleEntity.class, "class_id", entity.getId());
		List<ClassScheduleEntity> slots = toScheduleEntities(entity.getId(), classCourse.getSchedule());
		for (ClassScheduleEntity slot : slots) {
			dao.insertOrUpdate(slot);
		}

		return toBusinessModel(entity, slots);
	}

	private ClassCourse toBusinessModel(ClassCourseEntity entity, List<ClassScheduleEntity> slots) {
		List<ScheduleSlot> schedule = slots.stream()
				.map(slot -> new ScheduleSlot(slot.getDayOfWeek(), slot.getShift()))
				.toList();

		return new ClassCourse(
				entity.getId(),
				entity.getName(),
				entity.getCredits(),
				entity.getCapacity(),
				schedule,
				fromDbString(entity.getCreatedAt())
		);
	}

	private ClassCourseEntity toEntity(ClassCourse classCourse) {
		// a new class has no id yet, so keep the one the entity generated
		ClassCourseEntity entity = new ClassCourseEntity();
		if (classCourse.getId() != null) {
			entity.setId(classCourse.getId());
		}
		entity.setName(classCourse.getName());
		entity.setCredits(classCourse.getCredits());
		entity.setCapacity(classCourse.getCapacity());
		entity.setCreatedAt(toDbString(classCourse.getCreatedAt()));
		return entity;
	}

	private List<ClassScheduleEntity> toScheduleEntities(String classId, List<ScheduleSlot> schedule) {
		if (schedule == null) {
			return List.of();
		}
		return schedule.stream()
				.map(slot -> {
					ClassScheduleEntity entity = new ClassScheduleEntity();
					entity.setId(ClassScheduleEntity.idOf(classId, slot.dayOfWeek(), slot.shift()));
					entity.setClassId(classId);
					entity.setDayOfWeek(slot.dayOfWeek());
					entity.setShift(slot.shift());
					return entity;
				})
				.toList();
	}
}
