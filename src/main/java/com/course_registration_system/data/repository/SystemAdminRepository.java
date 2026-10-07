package com.course_registration_system.data.repository;

import com.course_registration_system.data.entity.SystemAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemAdminRepository extends JpaRepository<SystemAdminEntity, UUID> {
}
