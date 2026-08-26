package com.internship.recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "year", length = 20)
    private String year;

    @Column(name = "cgpa")
    private Double cgpa;

    @Column(name = "skills", length = 500)
    private String skills;

    @Column(name = "interests", length = 500)
    private String interests;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "profile_photo", length = 255)
    private String profilePhoto;
}
