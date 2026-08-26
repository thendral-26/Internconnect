package com.internship.recommendation.service;

import com.internship.recommendation.dto.RecommendationResultDTO;
import com.internship.recommendation.entity.Internship;
import com.internship.recommendation.entity.Recommendation;
import com.internship.recommendation.entity.Student;
import com.internship.recommendation.exception.StudentNotFoundException;
import com.internship.recommendation.repository.InternshipRepository;
import com.internship.recommendation.repository.RecommendationRepository;
import com.internship.recommendation.repository.StudentRepository;
import com.internship.recommendation.util.RecommendationEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final StudentRepository studentRepository;
    private final InternshipRepository internshipRepository;

    public RecommendationService(RecommendationRepository recommendationRepository,
                                  StudentRepository studentRepository,
                                  InternshipRepository internshipRepository) {
        this.recommendationRepository = recommendationRepository;
        this.studentRepository = studentRepository;
        this.internshipRepository = internshipRepository;
    }

    /**
     * Generate recommendations for a student.
     * 1. Load the student.
     * 2. Load all internships.
     * 3. Run the rule-based engine for each.
     * 4. Delete old recommendations for this student.
     * 5. Save new recommendations (match percentage only) to the DB.
     * 6. Return sorted result DTOs (highest match first).
     */
    public List<RecommendationResultDTO> generateRecommendations(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student with ID " + studentId + " not found."));

        List<Internship> internships = internshipRepository.findAll();

        // Calculate recommendation for each internship
        List<RecommendationResultDTO> results = internships.stream()
                .map(internship -> RecommendationEngine.calculate(student, internship))
                .sorted(Comparator.comparing(RecommendationResultDTO::getMatchPercentage).reversed())
                .collect(Collectors.toList());

        // Clear old recommendations
        recommendationRepository.deleteByStudentId(studentId);
        recommendationRepository.flush();

        // Save new recommendations to DB
        for (RecommendationResultDTO result : results) {
            Recommendation rec = new Recommendation();
            rec.setStudentId(studentId);
            rec.setInternshipId(result.getInternshipId());
            rec.setMatchPercentage(result.getMatchPercentage());
            recommendationRepository.save(rec);
        }

        return results;
    }

    /**
     * Get stored recommendations for a student, sorted by highest match percentage.
     */
    @Transactional(readOnly = true)
    public List<Recommendation> getRecommendationsByStudent(Long studentId) {
        return recommendationRepository.findByStudentIdOrderByMatchPercentageDesc(studentId);
    }

    /**
     * Get full recommendation result DTOs (with matching/missing skills) for display.
     * Recalculates to provide the skill breakdown, sorted highest match first.
     */
    @Transactional(readOnly = true)
    public List<RecommendationResultDTO> getRecommendationResults(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student with ID " + studentId + " not found."));

        List<Internship> internships = internshipRepository.findAll();

        return internships.stream()
                .map(internship -> RecommendationEngine.calculate(student, internship))
                .sorted(Comparator.comparing(RecommendationResultDTO::getMatchPercentage).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get only the recommended internships (match >= 70%).
     */
    @Transactional(readOnly = true)
    public List<RecommendationResultDTO> getRecommendedInternships(Long studentId) {
        return getRecommendationResults(studentId).stream()
                .filter(r -> "Recommended".equals(r.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Count total recommendations (dashboard stats).
     */
    public long countRecommendations() {
        return recommendationRepository.count();
    }
}
