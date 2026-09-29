package com.course_registration_system.entity;

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
public class CourseClass {
    @Id
    @Column(name = "class_id") // id created by system admin
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private int semester;

    @Column(nullable = false)
    private int year;

    private String section;

    private String schedule;

    private String room;

    @Column(nullable = false)
    private int capacity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
