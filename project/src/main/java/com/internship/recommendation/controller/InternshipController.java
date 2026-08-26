package com.internship.recommendation.controller;

import com.internship.recommendation.entity.Internship;
import com.internship.recommendation.service.InternshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/internships")
public class InternshipController {

    private final InternshipService internshipService;

    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    // ===== List all internships (with optional search + filter) =====

    @GetMapping
    public String listInternships(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) String location,
                                  @RequestParam(required = false) Double minimumCgpa,
                                  @RequestParam(required = false) String skills,
                                  Model model) {
        List<Internship> internships = internshipService.searchAndFilter(search, location, minimumCgpa, skills);

        model.addAttribute("internships", internships);
        model.addAttribute("search", search);
        model.addAttribute("location", location);
        model.addAttribute("minimumCgpa", minimumCgpa);
        model.addAttribute("skills", skills);
        model.addAttribute("pageTitle", "Internships");
        return "internship-list";
    }

    // ===== Internship details =====

    @GetMapping("/{id}")
    public String internshipDetails(@PathVariable Long id, Model model) {
        Internship internship = internshipService.getInternshipById(id);
        model.addAttribute("internship", internship);
        model.addAttribute("pageTitle", "Internship Details");
        return "internship-details";
    }

    // ===== Search by company name =====

    @GetMapping("/search")
    public String searchInternships(@RequestParam(required = false) String search, Model model) {
        List<Internship> internships = internshipService.searchByCompany(search);
        model.addAttribute("internships", internships);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Search Results");
        return "internship-list";
    }

    // ===== Filter by location / CGPA / skills =====

    @GetMapping("/filter")
    public String filterInternships(@RequestParam(required = false) String location,
                                    @RequestParam(required = false) Double minimumCgpa,
                                    @RequestParam(required = false) String skills,
                                    Model model) {
        List<Internship> internships = internshipService.searchAndFilter(null, location, minimumCgpa, skills);
        model.addAttribute("internships", internships);
        model.addAttribute("location", location);
        model.addAttribute("minimumCgpa", minimumCgpa);
        model.addAttribute("skills", skills);
        model.addAttribute("pageTitle", "Filter Results");
        return "internship-list";
    }
}
