package com.internship.recommendation.repository;

import com.internship.recommendation.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByCompanyNameContainingIgnoreCase(String companyName);

    List<Internship> findByLocationContainingIgnoreCase(String location);

    List<Internship> findByMinimumCgpaLessThanEqual(Double minimumCgpa);

    List<Internship> findByCompanyNameContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String companyName, String location);

    @Query("SELECT i FROM Internship i WHERE " +
           "(:companyName IS NULL OR :companyName = '' OR LOWER(i.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:location IS NULL OR :location = '' OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minCgpa IS NULL OR i.minimumCgpa <= :minCgpa) AND " +
           "(:skills IS NULL OR :skills = '' OR LOWER(i.requiredSkills) LIKE LOWER(CONCAT('%', :skills, '%')))")
    List<Internship> searchInternships(
        @Param("companyName") String companyName,
        @Param("location") String location,
        @Param("minCgpa") Double minCgpa,
        @Param("skills") String skills
    );
}
