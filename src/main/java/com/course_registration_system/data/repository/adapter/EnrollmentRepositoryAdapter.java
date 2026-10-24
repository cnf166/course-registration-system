package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.enrollment.EnrollmentRepository;
import com.course_registration_system.business.enrollment.model.Enrollment;
import com.course_registration_system.data.entity.EnrollmentEntity;
import org.springframework.stereotype.Repository;
import org.thingai.base.dao.Dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.course_registration_system.utils.TimeUtils.fromDbString;
import static com.course_registration_system.utils.TimeUtils.toDbString;

@Repository
public class EnrollmentRepositoryAdapter implements EnrollmentRepository {
	private final Dao dao;

	public EnrollmentRepositoryAdapter(Dao dao) {
		this.dao = dao;
	}

	@Override
	public List<Enrollment> findByStudent(String studentId) {
		return Arrays.stream(dao.query(EnrollmentEntity.class, "student_id", studentId))
				.map(this::toBusinessModel)
				.toList();
	}

	@Override
	public Optional<Enrollment> findByStudentAndClass(String studentId, String classId) {
		EnrollmentEntity[] rows = dao.query(
				EnrollmentEntity.class,
				new String[]{"student_id", "class_id"},
				new String[]{studentId, classId});
		return rows.length == 0 ? Optional.empty() : Optional.of(toBusinessModel(rows[0]));
	}

	@Override
	public int countByClass(String classId) {
		// counts loaded rows: at most `capacity` per class, and it avoids raw SQL with a client-supplied id
		return dao.query(EnrollmentEntity.class, "class_id", classId).length;
	}

	@Override
	public Enrollment save(Enrollment enrollment) {
		EnrollmentEntity entity = toEntity(enrollment);
		dao.insertOrUpdate(entity);
		return toBusinessModel(entity);
	}

	@Override
	public void delete(Enrollment enrollment) {
		dao.delete(EnrollmentEntity.class, enrollment.getId());
	}

	private Enrollment toBusinessModel(EnrollmentEntity entity) {
		return new Enrollment(
				entity.getId(),
				entity.getStudentId(),
				entity.getClassId(),
				fromDbString(entity.getEnrolledAt())
		);
	}

	private EnrollmentEntity toEntity(Enrollment enrollment) {
		// a new enrollment has no id yet, so keep the one the entity generated
		EnrollmentEntity entity = new EnrollmentEntity();
		if (enrollment.getId() != null) {
			entity.setId(enrollment.getId());
		}
		entity.setStudentId(enrollment.getStudentId());
		entity.setClassId(enrollment.getClassId());
		entity.setEnrolledAt(toDbString(enrollment.getEnrolledAt()));
		return entity;
	}
}
