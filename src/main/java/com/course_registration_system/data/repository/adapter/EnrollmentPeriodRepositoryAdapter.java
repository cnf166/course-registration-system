package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.enrollmentperiod.EnrollmentPeriodRepository;
import com.course_registration_system.business.enrollmentperiod.model.EnrollmentPeriod;
import com.course_registration_system.data.entity.EnrollmentPeriodEntity;
import org.springframework.stereotype.Repository;
import org.thingai.base.dao.Dao;

import java.util.Optional;

import static com.course_registration_system.utils.TimeUtils.fromDbString;
import static com.course_registration_system.utils.TimeUtils.toDbString;

// The table holds a single row with id EnrollmentPeriodEntity.CURRENT_ID.
@Repository
public class EnrollmentPeriodRepositoryAdapter implements EnrollmentPeriodRepository {
	private final Dao dao;

	public EnrollmentPeriodRepositoryAdapter(Dao dao) {
		this.dao = dao;
	}

	@Override
	public Optional<EnrollmentPeriod> findCurrent() {
		EnrollmentPeriodEntity[] rows = dao.query(EnrollmentPeriodEntity.class, "id", EnrollmentPeriodEntity.CURRENT_ID);
		return rows.length == 0 ? Optional.empty() : Optional.of(toBusinessModel(rows[0]));
	}

	@Override
	public EnrollmentPeriod save(EnrollmentPeriod period) {
		EnrollmentPeriodEntity entity = toEntity(period);
		dao.insertOrUpdate(entity);
		return toBusinessModel(entity);
	}

	private EnrollmentPeriod toBusinessModel(EnrollmentPeriodEntity entity) {
		return new EnrollmentPeriod(
				fromDbString(entity.getStartAt()),
				fromDbString(entity.getEndAt()),
				fromDbString(entity.getUpdatedAt())
		);
	}

	private EnrollmentPeriodEntity toEntity(EnrollmentPeriod period) {
		EnrollmentPeriodEntity entity = new EnrollmentPeriodEntity();
		entity.setId(EnrollmentPeriodEntity.CURRENT_ID);
		entity.setStartAt(toDbString(period.getStartAt()));
		entity.setEndAt(toDbString(period.getEndAt()));
		entity.setUpdatedAt(toDbString(period.getUpdatedAt()));
		return entity;
	}
}
