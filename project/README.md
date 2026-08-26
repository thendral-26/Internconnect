# Internship Recommendation System

A **Final Year CSE project** built with **Spring Boot + MySQL** that recommends internships to students based on their **Skills, CGPA, and Interests** using a simple **rule-based engine** (no AI / Machine Learning).

---

## Tech Stack

| Layer        | Technology                          |
|--------------|-------------------------------------|
| Frontend     | HTML5, CSS3, Bootstrap 5, JavaScript |
| Backend      | Java 21, Spring Boot, Spring MVC    |
| Database     | MySQL                               |
| ORM          | Spring Data JPA, Hibernate          |
| Build Tool   | Maven                               |
| Templating   | Thymeleaf                           |
| IDE          | IntelliJ IDEA                       |

---

## Project Objective

Develop a web application that recommends internships to students based on:

- **Skills** (60% weightage)
- **CGPA** (20% weightage)
- **Interests** (20% weightage)

The recommendation is **rule-based** — simple Java comparison logic. No AI, ML, or external APIs.

---

## User Roles

### Student
- Register / Login
- Update Profile (name, department, year, CGPA, skills, interests, phone)
- Upload Profile Photo
- View Recommended Internships
- View Internship Details
- Logout

### Admin
- Login
- Dashboard (statistics)
- Add / Edit / Delete Internship
- View All Students
- View All Recommendations
- Logout

---

## Recommendation Logic (Example)

**Student**
- CGPA = 8.4
- Skills: Java, MySQL, HTML, CSS
- Interest: Web Development

**Internship**
- Required Skills: Java, MySQL
- Minimum CGPA: 7.5
- Interest: Web Development

**Calculation**
- Skill Score = (2 / 2) × 60 = **60**
- CGPA Score = 8.4 ≥ 7.5 → **20**
- Interest Score = "Web Development" matches → **20**
- **Match Percentage = 100%** → **Recommended**

A match of **70% or above** is labeled **Recommended**. Below 70% is **Not Recommended**.

---

## Project Folder Structure

```
src/main/java/com/internship/recommendation/
├── controller/
│   ├── HomeController.java
│   ├── StudentController.java
│   ├── AdminController.java
│   ├── InternshipController.java
│   └── RecommendationController.java
├── service/
│   ├── StudentService.java
│   ├── AdminService.java
│   ├── InternshipService.java
│   └── RecommendationService.java
├── repository/
│   ├── StudentRepository.java
│   ├── AdminRepository.java
│   ├── InternshipRepository.java
│   └── RecommendationRepository.java
├── entity/
│   ├── Student.java
│   ├── Admin.java
│   ├── Internship.java
│   └── Recommendation.java
├── dto/
│   ├── StudentRegistrationDTO.java
│   ├── StudentLoginDTO.java
│   ├── AdminLoginDTO.java
│   ├── InternshipDTO.java
│   └── RecommendationResultDTO.java
├── config/
│   └── AppConfig.java
├── util/
│   └── RecommendationEngine.java
└── exception/
    ├── GlobalExceptionHandler.java
    ├── DuplicateEmailException.java
    ├── StudentNotFoundException.java
    └── InternshipNotFoundException.java

src/main/resources/
├── application.properties
├── schema.sql          (MySQL database schema)
├── data.sql            (sample data)
├── static/
│   ├── css/style.css
│   └── js/script.js
└── templates/
    ├── fragments/
    │   ├── head.html
    │   ├── navbar.html
    │   └── footer.html
    ├── home.html
    ├── student-login.html
    ├── student-register.html
    ├── student-dashboard.html
    ├── student-profile.html
    ├── edit-profile.html
    ├── internship-list.html
    ├── internship-details.html
    ├── recommended.html
    ├── recommendation-details.html
    ├── recommendations.html
    ├── admin-login.html
    ├── admin-dashboard.html
    ├── admin-internships.html
    ├── add-internship.html
    ├── edit-internship.html
    ├── students.html
    └── error.html
```

---

## Database Tables

### students
| Column         | Type         |
|----------------|--------------|
| id             | BIGINT (PK)  |
| name           | VARCHAR(100) |
| email          | VARCHAR(150) |
| password       | VARCHAR(100) |
| department     | VARCHAR(100) |
| year           | VARCHAR(20)  |
| cgpa           | DOUBLE       |
| skills         | VARCHAR(500) |
| interests      | VARCHAR(500) |
| phone          | VARCHAR(20)  |
| profile_photo  | VARCHAR(255) |

### admin
| Column   | Type         |
|----------|--------------|
| id       | BIGINT (PK)  |
| name     | VARCHAR(100) |
| email    | VARCHAR(150) |
| password | VARCHAR(100) |

### internship
| Column           | Type          |
|------------------|---------------|
| id               | BIGINT (PK)   |
| company_name     | VARCHAR(150)  |
| role             | VARCHAR(150)  |
| required_skills  | VARCHAR(500)  |
| minimum_cgpa     | DOUBLE        |
| interest         | VARCHAR(150)  |
| location         | VARCHAR(150)  |
| duration         | VARCHAR(100)  |
| stipend          | VARCHAR(100)  |
| description      | VARCHAR(2000) |
| last_date        | DATE          |

### recommendation
| Column            | Type    |
|-------------------|---------|
| id                | BIGINT  |
| student_id        | BIGINT  |
| internship_id     | BIGINT  |
| match_percentage  | DOUBLE  |

---

## Setup Instructions

### 1. Prerequisites
- Java 21
- MySQL 8+
- Maven
- IntelliJ IDEA (recommended)

### 2. Database Setup

Open MySQL and run:

```sql
SOURCE src/main/resources/schema.sql;
SOURCE src/main/resources/data.sql;
```

Or let Spring Boot create the tables automatically (`ddl-auto=update`), then run only `data.sql` for sample data.

### 3. Configure Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/internship_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or open in IntelliJ IDEA and run the main application class.

### 5. Access the Application

Open your browser and go to:

```
http://localhost:8080
```

---

## Sample Login Credentials

### Admin
- **Email:** admin@college.edu
- **Password:** admin123

### Students
| Email             | Password    |
|-------------------|-------------|
| rahul@example.com | student123  |
| priya@example.com | student123  |
| amit@example.com  | student123  |
| sneha@example.com | student123  |

---

## Features

- Responsive Bootstrap 5 design (Blue + White theme)
- Student registration with duplicate email check
- BCrypt password hashing
- Student profile with photo upload
- Rule-based recommendation engine
- Search internships by company name
- Filter by location, minimum CGPA, and skills
- Admin dashboard with statistics
- Add / edit / delete internships
- View all students and recommendations
- Success and error messages
- Form validation (email, password, required fields, CGPA range 0–10)
- Password show/hide toggle
- Auto-hide alerts
- Delete confirmation
- Profile photo preview before upload

---

## Key Files Explained

### pom.xml
Maven build configuration. Declares Spring Boot, Spring Data JPA, Thymeleaf, MySQL driver, Lombok, and Bean Validation dependencies.

### application.properties
Configures MySQL connection, JPA/Hibernate settings (ddl-auto=update), Thymeleaf, file upload limits, and server port.

### AppConfig.java
Spring configuration class that provides a BCryptPasswordEncoder bean for secure password hashing.

### Entity Classes
- **Student** — maps to the `students` table. Stores name, email, password, department, year, CGPA, skills, interests, phone, and profile photo filename.
- **Admin** — maps to the `admin` table. Stores name, email, and password.
- **Internship** — maps to the `internship` table. Stores company name, role, required skills, minimum CGPA, interest, location, duration, stipend, description, and last date.
- **Recommendation** — maps to the `recommendation` table. Stores student ID, internship ID, and match percentage.

### Repository Interfaces
Extend `JpaRepository` to provide CRUD operations and custom query methods (e.g., `findByEmail`, `findByCompanyNameContainingIgnoreCase`).

### Service Classes
Contain business logic:
- **StudentService** — registration, login, profile update, photo upload, student count.
- **AdminService** — admin login and lookup.
- **InternshipService** — CRUD, search, and filter operations.
- **RecommendationService** — generates recommendations by calling the engine, stores results, and retrieves them.

### RecommendationEngine.java
The core rule-based engine. Calculates match percentage using:
- Skill match (60%)
- CGPA eligibility (20%)
- Interest match (20%)

### Controller Classes
Handle HTTP requests and return Thymeleaf view names:
- **HomeController** — home page, login/register page routing.
- **StudentController** — student registration, login, dashboard, profile, photo upload, logout.
- **AdminController** — admin login, dashboard, student list, recommendation list, internship CRUD.
- **InternshipController** — internship listing, details, search, and filter.
- **RecommendationController** — student recommendation list and detail views.

### DTOs
Data Transfer Objects for form binding and result display:
- **StudentRegistrationDTO** — registration form fields with validation annotations.
- **StudentLoginDTO** — login form fields.
- **AdminLoginDTO** — admin login form fields.
- **InternshipDTO** — internship form fields with validation.
- **RecommendationResultDTO** — recommendation result with match percentage, status, matching/missing skills.

### Exception Classes
- **GlobalExceptionHandler** — catches custom exceptions and renders the error page.
- **DuplicateEmailException** — thrown when registering with an existing email.
- **StudentNotFoundException** — thrown when a student is not found.
- **InternshipNotFoundException** — thrown when an internship is not found.

### CSS (style.css)
Professional Blue + White theme. Responsive design with cards, navbar, tables, forms, alerts, progress bars, and hover effects.

### JavaScript (script.js)
Frontend functionality: delete confirmation, auto-hide alerts, password visibility toggle, CGPA validation, client-side search filtering, profile photo preview, last date validation, and form validation.

---

## Testing

Run the test suite:

```bash
mvn test
```

Manual testing checklist:
1. Register a new student
2. Login as student
3. Update profile with skills, CGPA, and interests
4. Upload a profile photo
5. View recommendations
6. Click an internship to view details
7. Login as admin
8. Add a new internship
9. Edit an internship
10. Delete an internship (with confirmation)
11. View all students
12. View all recommendations
13. Search internships by company
14. Filter by location, CGPA, and skills
