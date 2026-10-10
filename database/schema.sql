-- =======================================================================
-- SEA: SEAT EXAM ALLOTMENT SYSTEM
-- Relational Database Schema for MySQL 8.0
-- Matoshri College of Engineering and Research Centre, Eklahare, Nashik
-- Domain: @matoshriengg.edu.in
-- =======================================================================

CREATE DATABASE IF NOT EXISTS `sea_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `sea_db`;

-- Drop existing tables in reverse foreign-key dependency order
DROP TABLE IF EXISTS `attendance`;
DROP TABLE IF EXISTS `qr_verification`;
DROP TABLE IF EXISTS `seat_assignments`;
DROP TABLE IF EXISTS `exams`;
DROP TABLE IF EXISTS `halls`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `users`;

-- -----------------------------------------------------------------------
-- Table 1: users (Unified Authentication with BCrypt & Role-based Access)
-- -----------------------------------------------------------------------
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(64) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(120) NOT NULL UNIQUE,
    `full_name` VARCHAR(120) NOT NULL,
    `role` ENUM('ADMIN', 'STUDENT', 'INVIGILATOR') NOT NULL DEFAULT 'STUDENT',
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_users_role` (`role`),
    INDEX `idx_users_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 2: students (Academic records for seating & verification)
-- -----------------------------------------------------------------------
CREATE TABLE `students` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `roll_no` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(120) NOT NULL,
    `department` VARCHAR(64) NOT NULL, -- Computer Engineering, Artificial Intelligence & Data Science, Mechanical, Civil, Electrical, IT, Electronics & Telecommunication
    `year` INT NOT NULL,               -- 1, 2, 3, 4
    `semester` INT NOT NULL,           -- 1 through 8
    `email` VARCHAR(120),
    `phone` VARCHAR(20),
    `user_id` BIGINT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    INDEX `idx_students_roll` (`roll_no`),
    INDEX `idx_students_dept_year` (`department`, `year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 3: teachers (Invigilators and exam supervisors)
-- -----------------------------------------------------------------------
CREATE TABLE `teachers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `employee_id` VARCHAR(32) NOT NULL UNIQUE,
    `name` VARCHAR(120) NOT NULL,
    `department` VARCHAR(64) NOT NULL,
    `designation` VARCHAR(64) NOT NULL DEFAULT 'Assistant Professor',
    `email` VARCHAR(120),
    `phone` VARCHAR(20),
    `is_supervisor` BOOLEAN NOT NULL DEFAULT TRUE,
    `user_id` BIGINT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    INDEX `idx_teachers_emp` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 4: halls (Examination Halls with Matrix Dimensions)
-- -----------------------------------------------------------------------
CREATE TABLE `halls` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `hall_number` VARCHAR(32) NOT NULL UNIQUE,
    `block` VARCHAR(32) NOT NULL,
    `floor` VARCHAR(16) NOT NULL DEFAULT 'Ground',
    `total_rows` INT NOT NULL DEFAULT 5,
    `total_cols` INT NOT NULL DEFAULT 6,
    `capacity` INT GENERATED ALWAYS AS (`total_rows` * `total_cols`) STORED,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_halls_block` (`block`, `hall_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 5: exams (Scheduled Examinations)
-- -----------------------------------------------------------------------
CREATE TABLE `exams` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `exam_code` VARCHAR(32) NOT NULL UNIQUE,
    `title` VARCHAR(150) NOT NULL,
    `subject` VARCHAR(100) NOT NULL,
    `exam_date` DATE NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    `academic_year` VARCHAR(16) NOT NULL DEFAULT '2026-27',
    `semester` INT NOT NULL DEFAULT 4,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_exams_date` (`exam_date`),
    INDEX `idx_exams_code` (`exam_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 6: seat_assignments (Automated Anti-Malpractice Seating Records)
-- -----------------------------------------------------------------------
CREATE TABLE `seat_assignments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `exam_id` BIGINT NOT NULL,
    `hall_id` BIGINT NOT NULL,
    `supervisor_id` BIGINT NULL,
    `row_number` INT NOT NULL,
    `col_number` INT NOT NULL,
    `seat_number` VARCHAR(16) NOT NULL, -- e.g. R3-C5
    `qr_code_data` VARCHAR(255) NOT NULL,
    `qr_code_image` MEDIUMTEXT NULL,   -- Base64 encoded PNG
    `allotted_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_sa_student` FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sa_exam` FOREIGN KEY (`exam_id`) REFERENCES `exams`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sa_hall` FOREIGN KEY (`hall_id`) REFERENCES `halls`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sa_supervisor` FOREIGN KEY (`supervisor_id`) REFERENCES `teachers`(`id`) ON DELETE SET NULL,
    CONSTRAINT `uk_exam_student` UNIQUE (`exam_id`, `student_id`),
    CONSTRAINT `uk_exam_hall_seat` UNIQUE (`exam_id`, `hall_id`, `row_number`, `col_number`),
    INDEX `idx_sa_student_exam` (`student_id`, `exam_id`),
    INDEX `idx_sa_hall_exam` (`hall_id`, `exam_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 7: attendance (Contactless QR-Verified Real-Time Entry Logs)
-- -----------------------------------------------------------------------
CREATE TABLE `attendance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `seat_assignment_id` BIGINT NOT NULL UNIQUE,
    `student_id` BIGINT NOT NULL,
    `exam_id` BIGINT NOT NULL,
    `verified_by_teacher_id` BIGINT NULL,
    `status` ENUM('PRESENT', 'ABSENT', 'MALPRACTICE_FLAGGED') NOT NULL DEFAULT 'PRESENT',
    `scan_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `device_meta` VARCHAR(120) NULL,
    `remarks` VARCHAR(255) NULL,
    CONSTRAINT `fk_att_seat` FOREIGN KEY (`seat_assignment_id`) REFERENCES `seat_assignments`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_att_student` FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_att_exam` FOREIGN KEY (`exam_id`) REFERENCES `exams`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_att_teacher` FOREIGN KEY (`verified_by_teacher_id`) REFERENCES `teachers`(`id`) ON DELETE SET NULL,
    INDEX `idx_att_exam_status` (`exam_id`, `status`),
    INDEX `idx_att_student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------
-- Table 8: qr_verification (Tamper-Proof Audit Table for Digital Entry)
-- -----------------------------------------------------------------------
CREATE TABLE `qr_verification` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `qr_payload` VARCHAR(255) NOT NULL,
    `verification_hash` VARCHAR(64) NOT NULL,
    `student_roll` VARCHAR(32) NOT NULL,
    `exam_code` VARCHAR(32) NOT NULL,
    `is_used` BOOLEAN NOT NULL DEFAULT FALSE,
    `verified_at` TIMESTAMP NULL,
    `device_ip` VARCHAR(45) NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_qr_verification_hash` (`verification_hash`),
    INDEX `idx_qr_roll_exam` (`student_roll`, `exam_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
