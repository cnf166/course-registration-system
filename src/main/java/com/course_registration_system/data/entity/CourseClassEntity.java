package com.course_registration_system.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "course_class")
@Getter
@Setter
@NoArgsConstructor
public class CourseClassEntity {
    @Id
    @Column(name = "class_id") // id created by system admin
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_code", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "semester_id", nullable = false)
    private SemesterEntity semester;

    private String section;

    // as in define/Day.java
    @Column(name = "day_in_week", nullable = false)
    private int[] dayInWeek;

    // as in define/DayShift.java
    @Column(name = "day_shift", nullable = false)
    private int[] dayShift;

    private String room;

    @Column(nullable = false)
    private int capacity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
