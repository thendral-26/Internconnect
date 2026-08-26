package com.internship.recommendation.util;

import com.internship.recommendation.dto.RecommendationResultDTO;
import com.internship.recommendation.entity.Internship;
import com.internship.recommendation.entity.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pure rule-based recommendation engine.
 * No AI, ML, or external APIs — simple Java comparison logic.
 *
 * Weightage:
 *   Skills  = 60%
 *   CGPA    = 20%
 *   Interest = 20%
 */
public class RecommendationEngine {

    private static final double SKILL_WEIGHT = 60.0;
    private static final double CGPA_WEIGHT = 20.0;
    private static final double INTEREST_WEIGHT = 20.0;
    private static final double RECOMMEND_THRESHOLD = 70.0;

    private RecommendationEngine() {
    }

    /**
     * Calculate the recommendation result for a student-internship pair.
     *
     * Skill score = (matchedSkills / totalRequiredSkills) * 60
     * CGPA score  = student.cgpa >= internship.minimumCgpa ? 20 : 0
     * Interest    = student.interest matches internship.interest ? 20 : 0
     */
    public static RecommendationResultDTO calculate(Student student, Internship internship) {
        List<String> studentSkills = parseCsv(student.getSkills());
        List<String> requiredSkills = parseCsv(internship.getRequiredSkills());

        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String req : requiredSkills) {
            if (studentSkills.contains(req)) {
                matchingSkills.add(req);
            } else {
                missingSkills.add(req);
            }
        }

        // --- Skill score (60%) ---
        double skillScore = 0.0;
        if (!requiredSkills.isEmpty()) {
            skillScore = ((double) matchingSkills.size() / requiredSkills.size()) * SKILL_WEIGHT;
        }

        // --- CGPA score (20%) ---
        double cgpaScore = 0.0;
        if (student.getCgpa() != null && internship.getMinimumCgpa() != null
                && student.getCgpa() >= internship.getMinimumCgpa()) {
            cgpaScore = CGPA_WEIGHT;
        }

        // --- Interest score (20%) ---
        double interestScore = 0.0;
        if (student.getInterests() != null && internship.getInterest() != null) {
            List<String> studentInterests = parseCsv(student.getInterests());
            if (studentInterests.contains(internship.getInterest().trim().toLowerCase())) {
                interestScore = INTEREST_WEIGHT;
            }
        }

        double matchPercentage = round(skillScore + cgpaScore + interestScore);
        String status = matchPercentage >= RECOMMEND_THRESHOLD
                ? "Recommended" : "Not Recommended";

        return new RecommendationResultDTO(
                internship.getId(),
                internship,
                matchPercentage,
                status,
                matchingSkills,
                missingSkills
        );
    }

    /**
     * Parse a comma-separated string into a normalized lowercase list.
     */
    private static List<String> parseCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
