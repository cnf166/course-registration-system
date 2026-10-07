package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.user.UserRepository;
import com.course_registration_system.business.user.model.User;
import com.course_registration_system.data.entity.UserEntity;
import com.course_registration_system.data.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

	private final JpaUserRepository repository;

	public UserRepositoryAdapter(JpaUserRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<User> findById(UUID id) {
		return repository.findById(id)
				.map(this::toBusinessModel);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return repository.findByEmail(email)
				.map(this::toBusinessModel);
	}

	@Override
	public User save(User user) {
		UserEntity entity = toEntity(user);
		UserEntity savedEntity = repository.save(entity);
		// user (param tu business) --> chuyen thanh entity (infra) --> return lai duoi dang Model (business)
		return toBusinessModel(savedEntity);
	}

	// chuyen tu entity (infra/persistence layer --> business)
	private User toBusinessModel(UserEntity entity) {
		return new User(
				entity.getId(),
				entity.getName(),
				entity.getEmail(),
				entity.getPasswordHash(),
				entity.getRole(),
				entity.getCreatedAt()
		);
	}

	// chuyen tu model (business layer --> infra)
	private UserEntity toEntity(User user) {
		UserEntity entity = new UserEntity();

		entity.setId(user.getId());
		entity.setName(user.getName());
		entity.setEmail(user.getEmail());
		entity.setPasswordHash(user.getPasswordHash());
		entity.setRole(user.getRole());
		entity.setCreatedAt(user.getCreatedAt());

		return entity;
	}
}
