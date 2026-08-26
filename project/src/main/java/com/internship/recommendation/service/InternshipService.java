package com.internship.recommendation.service;

import com.internship.recommendation.dto.InternshipDTO;
import com.internship.recommendation.entity.Internship;
import com.internship.recommendation.exception.InternshipNotFoundException;
import com.internship.recommendation.repository.InternshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InternshipService {

    private final InternshipRepository internshipRepository;

    public InternshipService(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    /**
     * Add a new internship from DTO.
     */
    public Internship addInternship(InternshipDTO dto) {
        Internship internship = new Internship();
        mapDtoToEntity(dto, internship);
        return internshipRepository.save(internship);
    }

    /**
     * Get all internships.
     */
    @Transactional(readOnly = true)
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    /**
     * Get internship by ID.
     */
    @Transactional(readOnly = true)
    public Internship getInternshipById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new InternshipNotFoundException(
                        "Internship with ID " + id + " not found."));
    }

    /**
     * Update an existing internship.
     */
    public Internship updateInternship(Long id, InternshipDTO dto) {
        Internship existing = getInternshipById(id);
        mapDtoToEntity(dto, existing);
        return internshipRepository.save(existing);
    }

    /**
     * Delete an internship by ID.
     */
    public void deleteInternship(Long id) {
        Internship internship = getInternshipById(id);
        internshipRepository.delete(internship);
    }

    /**
     * Search internships by company name.
     */
    @Transactional(readOnly = true)
    public List<Internship> searchByCompany(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            return getAllInternships();
        }
        return internshipRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    /**
     * Filter internships by location.
     */
    @Transactional(readOnly = true)
    public List<Internship> filterByLocation(String location) {
        if (location == null || location.isBlank()) {
            return getAllInternships();
        }
        return internshipRepository.findByLocationContainingIgnoreCase(location);
    }

    /**
     * Filter internships by minimum CGPA.
     * Returns internships whose minimum CGPA is <= the given value
     * (i.e., internships the student is eligible for).
     */
    @Transactional(readOnly = true)
    public List<Internship> filterByMinimumCgpa(Double cgpa) {
        if (cgpa == null) {
            return getAllInternships();
        }
        return internshipRepository.findByMinimumCgpaLessThanEqual(cgpa);
    }

    /**
     * Filter internships by required skills.
     * Returns internships whose required skills contain at least one
     * of the provided skill keywords (case-insensitive).
     */
    @Transactional(readOnly = true)
    public List<Internship> filterBySkills(String skills) {
        if (skills == null || skills.isBlank()) {
            return getAllInternships();
        }
        List<String> skillKeywords = java.util.Arrays.stream(skills.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toList();

        return internshipRepository.findAll().stream()
                .filter(i -> {
                    String req = i.getRequiredSkills() == null
                            ? "" : i.getRequiredSkills().toLowerCase();
                    return skillKeywords.stream().anyMatch(req::contains);
                })
                .collect(Collectors.toList());
    }

    /**
     * Combined search and filter: company name + location.
     */
@Transactional(readOnly = true)
    public List<Internship> searchAndFilter(String companyName, String location, Double minimumCgpa, String skills) {
        return internshipRepository.searchInternships(companyName, location, minimumCgpa, skills);
    }

    /**
     * Count total internships (dashboard stats).
     */
    public long countInternships() {
        return internshipRepository.count();
    }

    /**
     * Map DTO fields to entity.
     */
    private void mapDtoToEntity(InternshipDTO dto, Internship entity) {
        entity.setCompanyName(dto.getCompanyName());
        entity.setRole(dto.getRole());
        entity.setRequiredSkills(dto.getRequiredSkills());
        entity.setMinimumCgpa(dto.getMinimumCgpa());
        entity.setInterest(dto.getInterest());
        entity.setLocation(dto.getLocation());
        entity.setDuration(dto.getDuration());
        entity.setStipend(dto.getStipend());
        entity.setDescription(dto.getDescription());
        entity.setLastDate(dto.getLastDate());
    }
}
