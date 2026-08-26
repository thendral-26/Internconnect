package com.internship.recommendation.controller;

import com.internship.recommendation.dto.RecommendationResultDTO;
import com.internship.recommendation.service.InternshipService;
import com.internship.recommendation.service.RecommendationService;
import com.internship.recommendation.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/student/recommendations")
public class RecommendationController {

    private final StudentService studentService;
    private final InternshipService internshipService;
    private final RecommendationService recommendationService;

    public RecommendationController(StudentService studentService,
                                    InternshipService internshipService,
                                    RecommendationService recommendationService) {
        this.studentService = studentService;
        this.internshipService = internshipService;
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public String recommendations(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        model.addAttribute("student", studentService.getStudentById(studentId));
        model.addAttribute("recommendations", recommendationService.generateRecommendations(studentId));
        model.addAttribute("pageTitle", "My Recommendations");
        return "recommended";
    }

    @GetMapping("/{internshipId}")
    public String recommendationDetails(@PathVariable Long internshipId,
                                         HttpSession session,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        Long studentId = (Long) session.getAttribute("studentId");
        if (studentId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/student/login";
        }

        Optional<RecommendationResultDTO> result = recommendationService.getRecommendationResults(studentId).stream()
                .filter(item -> internshipId.equals(item.getInternshipId()))
                .findFirst();
        if (result.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Internship not found.");
            return "redirect:/student/recommendations";
        }

        model.addAttribute("student", studentService.getStudentById(studentId));
        model.addAttribute("recommendation", result.get());
        model.addAttribute("pageTitle", "Recommendation Details");
        return "recommendation-details";
    }
}
