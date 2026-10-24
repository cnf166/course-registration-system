package com.course_registration_system.data.entity;

import com.course_registration_system.utils.IdUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thingai.base.dao.annotations.DaoColumn;
import org.thingai.base.dao.annotations.DaoTable;

@DaoTable(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @DaoColumn(name = "id", primaryKey = true)
    private String id = IdUtils.generateUuid(); // overwritten by the stored id when read from the DB

    // student code for students
    @DaoColumn(name = "username", nullable = false, unique = true)
    private String username;

    @DaoColumn(name = "name", nullable = false)
    private String name;

    @DaoColumn(name = "password_hash", nullable = false)
    private String passwordHash;

    // as in define/UserRole.java
    @DaoColumn(name = "role", nullable = false)
    private Integer role;

    @DaoColumn(name = "created_at", nullable = false)
    private String createdAt; // ISO-8601 UTC
}
