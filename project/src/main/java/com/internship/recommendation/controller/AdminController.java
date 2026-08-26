package com.internship.recommendation.controller;

import com.internship.recommendation.dto.AdminLoginDTO;
import com.internship.recommendation.dto.InternshipDTO;
import com.internship.recommendation.entity.Admin;
import com.internship.recommendation.entity.Recommendation;
import com.internship.recommendation.service.AdminService;
import com.internship.recommendation.service.InternshipService;
import com.internship.recommendation.service.RecommendationService;
import com.internship.recommendation.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final StudentService studentService;
    private final InternshipService internshipService;
    private final RecommendationService recommendationService;

    public AdminController(AdminService adminService,
                           StudentService studentService,
                           InternshipService internshipService,
                           RecommendationService recommendationService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.internshipService = internshipService;
        this.recommendationService = recommendationService;
    }

    // ===== Login =====

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("adminLoginDTO", new AdminLoginDTO());
        model.addAttribute("pageTitle", "Admin Login");
        return "admin-login";
    }

    @PostMapping("/login")
    public String loginAdmin(@Valid @ModelAttribute("adminLoginDTO") AdminLoginDTO dto,
                             BindingResult bindingResult,
                             HttpSession session,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        model.addAttribute("pageTitle", "Admin Login");

        if (bindingResult.hasErrors()) {
            return "admin-login";
        }

        var adminOpt = adminService.login(dto);
        if (adminOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid email or password.");
            return "admin-login";
        }

        session.setAttribute("adminId", adminOpt.get().getId());
        return "redirect:/admin/dashboard";
    }

    // ===== Dashboard =====

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        adminService.getAdminById(adminId).ifPresent(a -> model.addAttribute("admin", a));

        model.addAttribute("totalStudents", studentService.countStudents());
        model.addAttribute("totalInternships", internshipService.countInternships());
        model.addAttribute("totalRecommendations", recommendationService.countRecommendations());
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "admin-dashboard";
    }

    // ===== Students =====

    @GetMapping("/students")
    public String viewStudents(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("pageTitle", "All Students");
        return "students";
    }

    // ===== Recommendations =====

    @GetMapping("/recommendations")
    public String viewRecommendations(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        List<Recommendation> allRecommendations = new ArrayList<>();
        List<com.internship.recommendation.entity.Student> students = studentService.getAllStudents();
        Map<Long, com.internship.recommendation.entity.Student> studentsById = new HashMap<>();
        for (com.internship.recommendation.entity.Student student : students) {
            studentsById.put(student.getId(), student);
            allRecommendations.addAll(recommendationService.getRecommendationsByStudent(student.getId()));
        }
        Map<Long, com.internship.recommendation.entity.Internship> internshipsById = new HashMap<>();
        for (com.internship.recommendation.entity.Internship internship : internshipService.getAllInternships()) {
            internshipsById.put(internship.getId(), internship);
        }

        model.addAttribute("recommendations", allRecommendations);
        model.addAttribute("students", students);
        model.addAttribute("studentsById", studentsById);
        model.addAttribute("internshipsById", internshipsById);
        model.addAttribute("pageTitle", "All Recommendations");
        return "recommendations";
    }

    // ===== Internship Management =====

    @GetMapping("/internships")
    public String adminInternshipList(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        model.addAttribute("internships", internshipService.getAllInternships());
        model.addAttribute("pageTitle", "Manage Internships");
        return "admin-internships";
    }

    @GetMapping("/internships/add")
    public String showAddForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        model.addAttribute("internshipDTO", new InternshipDTO());
        model.addAttribute("pageTitle", "Add Internship");
        return "add-internship";
    }

    @PostMapping("/internships/add")
    public String addInternship(@Valid @ModelAttribute("internshipDTO") InternshipDTO dto,
                                BindingResult bindingResult,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Internship");
            return "add-internship";
        }

        internshipService.addInternship(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Internship added successfully.");
        return "redirect:/admin/internships";
    }

    @GetMapping("/internships/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        var internship = internshipService.getInternshipById(id);

        InternshipDTO dto = new InternshipDTO();
        dto.setId(internship.getId());
        dto.setCompanyName(internship.getCompanyName());
        dto.setRole(internship.getRole());
        dto.setRequiredSkills(internship.getRequiredSkills());
        dto.setMinimumCgpa(internship.getMinimumCgpa());
        dto.setInterest(internship.getInterest());
        dto.setLocation(internship.getLocation());
        dto.setDuration(internship.getDuration());
        dto.setStipend(internship.getStipend());
        dto.setDescription(internship.getDescription());
        dto.setLastDate(internship.getLastDate());

        model.addAttribute("internshipDTO", dto);
        model.addAttribute("pageTitle", "Edit Internship");
        return "edit-internship";
    }

    @PostMapping("/internships/edit/{id}")
    public String updateInternship(@PathVariable Long id,
                                   @Valid @ModelAttribute("internshipDTO") InternshipDTO dto,
                                   BindingResult bindingResult,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Internship");
            return "edit-internship";
        }

        internshipService.updateInternship(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Internship updated successfully.");
        return "redirect:/admin/internships";
    }

    @PostMapping("/internships/delete/{id}")
    public String deleteInternship(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to access this page.");
            return "redirect:/admin/login";
        }

        internshipService.deleteInternship(id);
        redirectAttributes.addFlashAttribute("successMessage", "Internship deleted successfully.");
        return "redirect:/admin/internships";
    }

    // ===== Logout =====

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
