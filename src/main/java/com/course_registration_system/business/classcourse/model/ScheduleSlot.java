package com.course_registration_system.business.classcourse.model;

// One weekly session; dayOfWeek as in data/define/Day.java, shift as in data/define/DayShift.java.
public record ScheduleSlot(int dayOfWeek, int shift) {
}
