-- ============================================================
-- Internship Recommendation System - Sample Data
-- ============================================================
-- Run AFTER schema.sql.
-- Passwords are BCrypt hashes:
--   admin@college.edu  -> password "admin123"
--   student emails     -> password "student123"
-- ============================================================

USE internship_db;

-- ---------- Admin ----------
INSERT INTO admin (name, email, password) VALUES
('Admin', 'admin@college.edu',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- ---------- Students ----------
INSERT INTO students (name, email, password, department, year, cgpa, skills, interests, phone) VALUES
('Rahul Sharma', 'rahul@example.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Computer Science', 'Final Year', 8.4,
 'Java,MySQL,HTML,CSS', 'Web Development', '9876543210'),

('Priya Patel', 'priya@example.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Information Technology', 'Final Year', 9.1,
 'Python,Machine Learning,Pandas,NumPy', 'Data Science', '9876543211'),

('Amit Kumar', 'amit@example.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Computer Science', 'Pre-Final Year', 7.2,
 'Java,Spring,HTML,JavaScript', 'Backend Development', '9876543212'),

('Sneha Reddy', 'sneha@example.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'Electronics', 'Final Year', 8.0,
 'C,C++,Embedded Systems,Arduino', 'Embedded Systems', '9876543213');

-- ---------- Internships ----------
INSERT INTO internship (company_name, role, required_skills, minimum_cgpa, interest, location, duration, stipend, description, last_date) VALUES
('Tech Solutions Pvt Ltd', 'Software Engineering Intern',
 'Java,MySQL,HTML,CSS', 7.5, 'Web Development',
 'Bangalore', '3 months', '15,000/month',
 'Work on building REST APIs and web applications using Java and Spring Boot. You will learn industry best practices and agile methodology.',
 '2025-12-31'),

('DataCorp Analytics', 'Data Science Intern',
 'Python,Pandas,NumPy,Machine Learning', 8.0, 'Data Science',
 'Hyderabad', '6 months', '25,000/month',
 'Analyze large datasets, build predictive models, and present insights to stakeholders. Strong Python skills required.',
 '2025-11-30'),

('InnovateSoft', 'Backend Developer Intern',
 'Java,Spring,JavaScript,HTML', 7.0, 'Backend Development',
 'Pune', '4 months', '20,000/month',
 'Develop and maintain backend services using Spring Boot. Learn microservices architecture and REST API design.',
 '2026-01-15'),

('EmbeddedTech Systems', 'Embedded Systems Intern',
 'C,C++,Embedded Systems,Arduino', 7.5, 'Embedded Systems',
 'Chennai', '3 months', '18,000/month',
 'Work on IoT devices and embedded firmware development. Hands-on experience with microcontrollers and real-time systems.',
 '2025-12-15'),

('CloudNet Technologies', 'Full Stack Developer Intern',
 'Java,HTML,CSS,JavaScript,MySQL', 8.0, 'Web Development',
 'Remote', '6 months', '22,000/month',
 'End-to-end web development using Java backend and modern frontend. Great opportunity to learn full stack development.',
 '2026-02-28'),

('AI Research Labs', 'Machine Learning Intern',
 'Python,Machine Learning,Deep Learning,TensorFlow', 8.5, 'Data Science',
 'Bangalore', '6 months', '30,000/month',
 'Research and develop ML models for NLP and computer vision applications. Publish findings in internal reports.',
 '2026-01-31');
