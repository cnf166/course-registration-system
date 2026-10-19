package com.course_registration_system.business.courseclass.model;

import java.time.DayOfWeek;

// One weekly session. Null day or shift means the class has no fixed day or shift.
public record ScheduleSlot(DayOfWeek day, Integer shift) {
}
