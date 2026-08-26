-- ============================================================
-- Internship Recommendation System - MySQL Schema
-- Final Year CSE Project
-- ============================================================
-- Run this script in MySQL to create the database and tables.
-- Alternatively, Spring Boot's ddl-auto=update will create
-- tables automatically when the app starts.
-- ============================================================

CREATE DATABASE IF NOT EXISTS internship_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE internship_db;

-- ---------- Students table ----------
DROP TABLE IF EXISTS recommendation;
DROP TABLE IF EXISTS internship;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS admin;

CREATE TABLE students (
    id           BIGINT NOT NULL AUTO_INCREMENT,
    name         VARCHAR(100)  NOT NULL,
    email        VARCHAR(150)  NOT NULL UNIQUE,
    password     VARCHAR(100)  NOT NULL,
    department   VARCHAR(100),
    year         VARCHAR(20),
    cgpa         DOUBLE,
    skills       VARCHAR(500),
    interests    VARCHAR(500),
    phone        VARCHAR(20),
    profile_photo VARCHAR(255),
    PRIMARY KEY (id)
);

-- ---------- Admin table ----------
CREATE TABLE admin (
    id       BIGINT NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- ---------- Internship table ----------
CREATE TABLE internship (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    company_name   VARCHAR(150) NOT NULL,
    role           VARCHAR(150) NOT NULL,
    required_skills VARCHAR(500) NOT NULL,
    minimum_cgpa   DOUBLE NOT NULL,
    interest       VARCHAR(150) NOT NULL,
    location       VARCHAR(150),
    duration       VARCHAR(100),
    stipend        VARCHAR(100),
    description    VARCHAR(2000),
    last_date      DATE,
    PRIMARY KEY (id)
);

-- ---------- Recommendation table ----------
CREATE TABLE recommendation (
    id               BIGINT NOT NULL AUTO_INCREMENT,
    student_id       BIGINT NOT NULL,
    internship_id   BIGINT NOT NULL,
    match_percentage DOUBLE NOT NULL,
    PRIMARY KEY (id)
);
