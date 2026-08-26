package com.internship.recommendation.dto;

import com.internship.recommendation.entity.Internship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResultDTO {

    private Long internshipId;
    private Internship internship;
    private Double matchPercentage;
    private String status;
    private List<String> matchingSkills;
    private List<String> missingSkills;
}
