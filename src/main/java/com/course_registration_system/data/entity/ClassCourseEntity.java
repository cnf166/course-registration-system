package com.course_registration_system.data.entity;

import com.course_registration_system.utils.IdUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thingai.base.dao.annotations.DaoColumn;
import org.thingai.base.dao.annotations.DaoTable;

@DaoTable(name = "classes")
@Getter
@Setter
@NoArgsConstructor
public class ClassCourseEntity {
    @DaoColumn(name = "id", primaryKey = true)
    private String id = IdUtils.generateUuid(); // overwritten by the stored id when read from the DB

    @DaoColumn(name = "name", nullable = false)
    private String name;

    @DaoColumn(name = "credits", nullable = false)
    private Integer credits;

    @DaoColumn(name = "capacity", nullable = false)
    private Integer capacity;

    @DaoColumn(name = "created_at", nullable = false)
    private String createdAt; // ISO-8601 UTC
}
