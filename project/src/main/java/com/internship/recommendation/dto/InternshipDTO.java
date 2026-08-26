package com.internship.recommendation.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InternshipDTO {

    private Long id;

    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name is too long")
    private String companyName;

    @NotBlank(message = "Role is required")
    @Size(max = 150, message = "Role is too long")
    private String role;

    @NotBlank(message = "Required skills are required")
    @Size(max = 500, message = "Required skills list is too long")
    private String requiredSkills;

    @NotNull(message = "Minimum CGPA is required")
    @DecimalMin(value = "0.0", message = "Minimum CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Minimum CGPA must be at most 10.0")
    private Double minimumCgpa;

    @NotBlank(message = "Interest is required")
    @Size(max = 150, message = "Interest is too long")
    private String interest;

    @Size(max = 150, message = "Location is too long")
    private String location;

    @Size(max = 100, message = "Duration is too long")
    private String duration;

    @Size(max = 100, message = "Stipend is too long")
    private String stipend;

    @Size(max = 2000, message = "Description is too long")
    private String description;

    private LocalDate lastDate;
}
