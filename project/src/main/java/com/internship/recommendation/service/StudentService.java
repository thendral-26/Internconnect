package com.internship.recommendation.service;

import com.internship.recommendation.dto.StudentLoginDTO;
import com.internship.recommendation.dto.StudentRegistrationDTO;
import com.internship.recommendation.entity.Student;
import com.internship.recommendation.exception.DuplicateEmailException;
import com.internship.recommendation.exception.StudentNotFoundException;
import com.internship.recommendation.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/uploads/";

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new student.
     * Checks for duplicate email before saving.
     * Hashes the password with BCrypt.
     */
    public Student register(StudentRegistrationDTO dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("A student with email " + dto.getEmail() + " already exists.");
        }

        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setDepartment(dto.getDepartment());
        student.setYear(dto.getYear());
        student.setCgpa(dto.getCgpa());
        student.setSkills(dto.getSkills());
        student.setInterests(dto.getInterests());
        student.setPhone(dto.getPhone());
        student.setProfilePhoto(null);

        return studentRepository.save(student);
    }

    /**
     * Verify student login credentials.
     * Returns the student if email exists and password matches.
     */
    public Optional<Student> login(StudentLoginDTO dto) {
        Optional<Student> studentOpt = studentRepository.findByEmail(dto.getEmail());
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (passwordEncoder.matches(dto.getPassword(), student.getPassword())) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    /**
     * Get a student by ID.
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + id + " not found."));
    }

    /**
     * Get a student by email.
     */
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    /**
     * Update a student's profile.
     * Does not change the password or email.
     */
    public Student updateProfile(Long id, Student updated) {
        Student existing = getStudentById(id);

        existing.setName(updated.getName());
        existing.setDepartment(updated.getDepartment());
        existing.setYear(updated.getYear());
        existing.setCgpa(updated.getCgpa());
        existing.setSkills(updated.getSkills());
        existing.setInterests(updated.getInterests());
        existing.setPhone(updated.getPhone());

        return studentRepository.save(existing);
    }

    /**
     * Save a profile photo.
     * Returns the generated filename.
     */
    public String saveProfilePhoto(Long studentId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = "student_" + studentId + "_" + UUID.randomUUID() + extension;
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));

        Student student = getStudentById(studentId);
        student.setProfilePhoto(filename);
        studentRepository.save(student);

        return filename;
    }

    /**
     * Get all students (for admin view).
     */
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Count total students (dashboard stats).
     */
    public long countStudents() {
        return studentRepository.count();
    }

    /**
     * Check if email already exists.
     */
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
}
