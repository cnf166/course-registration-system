package com.course_registration_system.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
    name = "semester",
    uniqueConstraints = @UniqueConstraint(columnNames = {"year", "term"}),
    indexes = @Index(name = "uk_semester_current", columnList = "is_current", unique = true, options = "where is_current")
)
@Getter
@Setter
@NoArgsConstructor
public class SemesterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "semester_id")
    private UUID id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int term;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_current", nullable = false)
    private boolean current;
}
