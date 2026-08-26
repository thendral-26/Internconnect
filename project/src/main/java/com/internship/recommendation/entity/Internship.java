package com.internship.recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "internship")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "role", nullable = false, length = 150)
    private String role;

    @Column(name = "required_skills", nullable = false, length = 500)
    private String requiredSkills;

    @Column(name = "minimum_cgpa", nullable = false)
    private Double minimumCgpa;

    @Column(name = "interest", nullable = false, length = 150)
    private String interest;

    @Column(name = "location", length = 150)
    private String location;

    @Column(name = "duration", length = 100)
    private String duration;

    @Column(name = "stipend", length = 100)
    private String stipend;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "last_date")
    private LocalDate lastDate;
}
