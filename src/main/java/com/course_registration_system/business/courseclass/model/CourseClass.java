package com.course_registration_system.business.courseclass.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseClass {
	private String id;
	private String courseCode;
	private UUID semesterId;
	private String section;
	private List<ScheduleSlot> schedule;
	private String room;
	private int capacity;
	private Instant createdAt;
}
