package com.internship.recommendation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommendation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "internship_id", nullable = false)
    private Long internshipId;

    @Column(name = "match_percentage", nullable = false)
    private Double matchPercentage;
}
