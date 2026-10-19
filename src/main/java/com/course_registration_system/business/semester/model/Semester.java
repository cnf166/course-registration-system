package com.course_registration_system.business.semester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
	private UUID id;
	private int year;
	private int term;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean current;
}
