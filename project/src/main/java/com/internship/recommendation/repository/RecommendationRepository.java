package com.internship.recommendation.repository;

import com.internship.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByStudentIdOrderByMatchPercentageDesc(Long studentId);

    List<Recommendation> findByStudentId(Long studentId);

    void deleteByStudentId(Long studentId);

    long count();
}
