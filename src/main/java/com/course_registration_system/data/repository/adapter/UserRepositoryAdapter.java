package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.user.UserRepository;
import com.course_registration_system.business.user.model.User;
import com.course_registration_system.data.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.thingai.base.dao.Dao;

import java.util.Optional;

import static com.course_registration_system.utils.TimeUtils.fromDbString;
import static com.course_registration_system.utils.TimeUtils.toDbString;

@Repository
public class UserRepositoryAdapter implements UserRepository {
	private final Dao dao;

	public UserRepositoryAdapter(Dao dao) {
		this.dao = dao;
	}

	@Override
	public Optional<User> findById(String id) {
		return findOneBy("id", id);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return findOneBy("username", username);
	}

	@Override
	public boolean existsByUsername(String username) {
		return findByUsername(username).isPresent();
	}

	@Override
	public User save(User user) {
		UserEntity entity = toEntity(user);
		dao.insertOrUpdate(entity);
		return toBusinessModel(entity);
	}

	private Optional<User> findOneBy(String column, String value) {
		UserEntity[] rows = dao.query(UserEntity.class, column, value);
		return rows.length == 0 ? Optional.empty() : Optional.of(toBusinessModel(rows[0]));
	}

	private User toBusinessModel(UserEntity entity) {
		return new User(
				entity.getId(),
				entity.getUsername(),
				entity.getName(),
				entity.getPasswordHash(),
				entity.getRole(),
				fromDbString(entity.getCreatedAt())
		);
	}

	private UserEntity toEntity(User user) {
		// a new user has no id yet, so keep the one the entity generated
		UserEntity entity = new UserEntity();
		if (user.getId() != null) {
			entity.setId(user.getId());
		}
		entity.setUsername(user.getUsername());
		entity.setName(user.getName());
		entity.setPasswordHash(user.getPasswordHash());
		entity.setRole(user.getRole());
		entity.setCreatedAt(toDbString(user.getCreatedAt()));
		return entity;
	}
}
