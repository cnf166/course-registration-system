package com.course_registration_system.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "system_admin")
@Getter
@Setter
@NoArgsConstructor
public class SystemAdmin {
    @Id
    @Column(name = "user_id")
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
