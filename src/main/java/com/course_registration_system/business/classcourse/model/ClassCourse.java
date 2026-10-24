package com.course_registration_system.business.classcourse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassCourse {
	private String id;
	private String name;
	private int credits;
	private int capacity;
	private List<ScheduleSlot> schedule;
	private Instant createdAt;
}
