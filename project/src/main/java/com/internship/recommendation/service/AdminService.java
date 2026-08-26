package com.internship.recommendation.service;

import com.internship.recommendation.dto.AdminLoginDTO;
import com.internship.recommendation.entity.Admin;
import com.internship.recommendation.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verify admin login credentials.
     */
    public Optional<Admin> login(AdminLoginDTO dto) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(dto.getEmail());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
                return Optional.of(admin);
            }
        }
        return Optional.empty();
    }

    /**
     * Get admin by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * Get admin by email.
     */
    @Transactional(readOnly = true)
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
