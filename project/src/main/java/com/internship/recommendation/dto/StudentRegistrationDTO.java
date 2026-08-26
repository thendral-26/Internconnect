package com.internship.recommendation.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentRegistrationDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    @Size(max = 150, message = "Email is too long")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department is too long")
    private String department;

    @NotBlank(message = "Year is required")
    @Size(max = 20, message = "Year is too long")
    private String year;

    @NotNull(message = "CGPA is required")
    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA must be at most 10.0")
    private Double cgpa;

    @Size(max = 500, message = "Skills list is too long")
    private String skills;

    @Size(max = 500, message = "Interests list is too long")
    private String interests;

    @Pattern(regexp = "^[6-9]\\d{9}$|^[0-9+\\-\\s]{10,15}$",
             message = "Please enter a valid 10-digit phone number")
    private String phone;
}
