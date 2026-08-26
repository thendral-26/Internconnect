package com.internship.recommendation.controller;

import com.internship.recommendation.dto.StudentLoginDTO;
import com.internship.recommendation.dto.StudentRegistrationDTO;
import com.internship.recommendation.entity.Student;
import com.internship.recommendation.service.InternshipService;
import com.internship.recommendation.service.RecommendationService;
import com.internship.recommendation.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final InternshipService internshipService;
    private final RecommendationService recommendationService;

    public StudentController(StudentService studentService,
                             InternshipService internshipService,
                             RecommendationService recommendationService) {
        this.studentService = studentService;
        this.internshipService = internshipService;
        this.recommendationService = recommendationService;
    }

    // ===== Register =====

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("studentRegistrationDTO", new StudentRegistrationDTO());
        model.addAttribute("pageTitle", "Student Registration");
        return "student-register";
    }

    @PostMapping("/register")
    public String registerStudent(@Valid @ModelAttribute("studentRegistrationDTO") StudentRegistrationDTO dto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        model.addAttribute("pageTitle", "Student Registration");

        if (bindingResult.hasErrors()) {
            return "student-register";
        }

        if (studentService.emailExists(dto.getEmail())) {
            model.addAttribute("errorMessage", "Email already registered. Please use a different email.");
            return "student-register";
        }

        studentService.register(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful. Please login.");
        return "redirect:/student/login";
    }

    // ===== Login =====

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("studentLoginDTO", new StudentLoginDTO());
        model.addAttribute("pageTitle", "Student Login");
        return "student-login";
    }

    @PostMapping("/login")
    public String loginStudent(@Valid @ModelAttribute("studentLoginDTO") StudentLoginDTO dto,
                               BindingResult bindingResult,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        model.addAttribute("pageTitle", "Student Login");

        if (bindingResult.hasErrors()) {
            return "student-login";
        }

        var studentOpt = studentService.login(dto);
        if (studentOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid email or password.");
            return "student-login";
        }

        session.setAttribute("studentId", studentOpt.get().getId());
        return "redirect:/student/dashboard";
    }

    // ===== Dashboard =====

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        Student student = studentService.getStudentById(studentId);
        long totalInternships = internshipService.countInternships();
        var recommendations = recommendationService.getRecommendationResults(studentId);
        long recommendedCount = recommendations.stream()
                .filter(r -> "Recommended".equals(r.getStatus()))
                .count();

        model.addAttribute("student", student);
        model.addAttribute("totalInternships", totalInternships);
        model.addAttribute("totalRecommendations", (long) recommendations.size());
        model.addAttribute("recommendedCount", recommendedCount);
        model.addAttribute("pageTitle", "Student Dashboard");
        return "student-dashboard";
    }

    // ===== Profile =====

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        Student student = studentService.getStudentById(studentId);
        model.addAttribute("student", student);
        model.addAttribute("pageTitle", "My Profile");
        return "student-profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        Student student = studentService.getStudentById(studentId);
        model.addAttribute("student", student);
        model.addAttribute("pageTitle", "Edit Profile");
        return "edit-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(HttpSession session,
                                @ModelAttribute Student updated,
                                RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        studentService.updateProfile(studentId, updated);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        return "redirect:/student/profile";
    }

    @PostMapping("/profile/photo")
    public String uploadProfilePhoto(HttpSession session,
                                     @RequestParam("photo") MultipartFile file,
                                     RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a photo to upload.");
            return "redirect:/student/profile";
        }

        try {
            studentService.saveProfilePhoto(studentId, file);
            redirectAttributes.addFlashAttribute("successMessage", "Profile photo uploaded successfully.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload photo. Please try again.");
        }
        return "redirect:/student/profile";
    }

    // ===== Logout =====

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
