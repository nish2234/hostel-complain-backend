package com.vit.hostel.entity;

import com.vit.hostel.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hostels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(name = "admin_email", nullable = false, unique = true)
    private String adminEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
}
