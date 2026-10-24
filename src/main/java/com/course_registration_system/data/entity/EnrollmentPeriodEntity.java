package com.course_registration_system.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thingai.base.dao.annotations.DaoColumn;
import org.thingai.base.dao.annotations.DaoTable;

@DaoTable(name = "enrollment_period")
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentPeriodEntity {
    // single shared period for all students, so the table holds only this row
    public static final String CURRENT_ID = "current";

    @DaoColumn(name = "id", primaryKey = true)
    private String id;

    @DaoColumn(name = "start_at", nullable = false)
    private String startAt; // ISO-8601 UTC, inclusive

    @DaoColumn(name = "end_at", nullable = false)
    private String endAt; // ISO-8601 UTC, exclusive

    @DaoColumn(name = "updated_at", nullable = false)
    private String updatedAt; // ISO-8601 UTC
}
