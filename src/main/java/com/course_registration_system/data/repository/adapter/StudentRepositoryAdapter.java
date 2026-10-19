package com.course_registration_system.data.repository.adapter;

import com.course_registration_system.business.user.StudentRepository;
import com.course_registration_system.business.user.model.Student;
import com.course_registration_system.data.entity.StudentEntity;
import com.course_registration_system.data.repository.JpaStudentRepository;

import java.util.Optional;
import java.util.UUID;

public class StudentRepositoryAdapter implements StudentRepository {
	private final JpaStudentRepository repository;

	public StudentRepositoryAdapter(JpaStudentRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<Student> findById(UUID id) {
		return repository.findById(id)
				.map(this::toBusinessModel);
	}

	@Override
	public Optional<Student> findByStudentId(String studentId) {
		return repository.findByStudentId(studentId)
				.map(this::toBusinessModel);
	}

	@Override
	public Student save(Student student) {
		StudentEntity studentEntity = toEntity(student);
		StudentEntity savedStudentEntity = repository.save(studentEntity);
		return toBusinessModel(savedStudentEntity);
	}

	// map: infra --> business --> return business model
	private Student toBusinessModel(StudentEntity entity) {
		return new Student(
				entity.getId(),
				entity.getMajor(),
				entity.getStudentId()
		);
	}

	// map: business model --> infra entity (copy tung field) --> return infra

	private StudentEntity toEntity(Student student) {
		StudentEntity studentEntity = new StudentEntity();

		studentEntity.setId(student.getId());
		studentEntity.setMajor(student.getMajor());
		studentEntity.setStudentId(student.getStudentId());

		return studentEntity;
	}

}
