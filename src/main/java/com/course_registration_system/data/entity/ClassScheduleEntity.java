package com.course_registration_system.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thingai.base.dao.annotations.DaoColumn;
import org.thingai.base.dao.annotations.DaoTable;

@DaoTable(name = "class_schedules")
@Getter
@Setter
@NoArgsConstructor
public class ClassScheduleEntity {
    // {class_id}:{day_of_week}:{shift}, so a class can't hold the same slot twice
    @DaoColumn(name = "id", primaryKey = true)
    private String id;

    @DaoColumn(name = "class_id", nullable = false)
    private String classId;

    // as in define/Day.java
    @DaoColumn(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    // as in define/DayShift.java
    @DaoColumn(name = "shift", nullable = false)
    private Integer shift;

    /**
     * Builds the primary key of a schedule slot.
     * The key is derived from the slot because DaoSqlite has no composite unique constraint.
     */
    public static String idOf(String classId, int dayOfWeek, int shift) {
        return classId + ":" + dayOfWeek + ":" + shift;
    }
}
