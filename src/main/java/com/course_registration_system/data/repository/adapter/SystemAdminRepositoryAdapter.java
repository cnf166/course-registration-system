package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.user.SystemAdminRepository;
import com.course_registration_system.business.user.model.Student;
import com.course_registration_system.business.user.model.SystemAdmin;
import com.course_registration_system.data.entity.StudentEntity;
import com.course_registration_system.data.entity.SystemAdminEntity;
import com.course_registration_system.data.repository.JpaSystemAdminRepository;

import java.util.Optional;
import java.util.UUID;

public class SystemAdminRepositoryAdapter implements SystemAdminRepository {
	private final JpaSystemAdminRepository repository;

	public SystemAdminRepositoryAdapter(JpaSystemAdminRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<SystemAdmin> findById(UUID id) {
		return repository.findById(id)
				.map(this::toBusinessModel);
	}

	@Override
	public SystemAdmin save(SystemAdmin systemAdmin) {
		SystemAdminEntity systemAdminEntity = toEntity(systemAdmin);
		SystemAdminEntity savedSystemAdminEntity = repository.save(systemAdminEntity);
		return toBusinessModel(savedSystemAdminEntity);
	}

	// map: infra --> business --> return business model
	private SystemAdmin toBusinessModel(SystemAdminEntity entity) {
		return new SystemAdmin(
				entity.getId()
		);
	}

	// map: business model --> infra entity (copy tung field) --> return infra

	private SystemAdminEntity toEntity(SystemAdmin systemAdmin) {
		SystemAdminEntity systemAdminEntity = new SystemAdminEntity();

		systemAdminEntity.setId(systemAdmin.getId());

		return systemAdminEntity;
	}

}
