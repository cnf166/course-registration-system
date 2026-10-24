package com.course_registration_system.data.entity;

import com.course_registration_system.utils.IdUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thingai.base.dao.annotations.DaoColumn;
import org.thingai.base.dao.annotations.DaoTable;

@DaoTable(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentEntity {
    @DaoColumn(name = "id", primaryKey = true)
    private String id = IdUtils.generateUuid();

    @DaoColumn(name = "student_id", nullable = false)
    private String studentId;

    @DaoColumn(name = "class_id", nullable = false)
    private String classId;

    @DaoColumn(name = "enrolled_at", nullable = false)
    private String enrolledAt; // ISO-8601 UTC
}
