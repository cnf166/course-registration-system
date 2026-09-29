package com.course_registration_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "enrollment",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "class_id"}) // A student can enroll in the same class only once
)
@Getter
@Setter
@NoArgsConstructor
public class CourseClassEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "enrollment_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private UserStudent student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private CourseClass courseClass;

    @CreationTimestamp
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private Instant enrolledAt;

    @Column(nullable = false)
    private String status;
}
