-- =======================================================================
-- SEA: SEAT EXAM ALLOTMENT SYSTEM
-- Seed Sample Data for MySQL 8.0
-- Matoshri College of Engineering and Research Centre, Eklahare, Nashik
-- Domain: @matoshriengg.edu.in
-- Standard BCrypt Hash for passwords:
-- 'admin123'       -> $2a$10$7EqJtq98hPqEX7fNZaFWoO.8v9gY.c80Q7bV9g6oG7Y8xK9Z3P6uq
-- 'invigilator123' -> $2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.
-- 'student123'     -> $2a$10$w095i3bI0zD9mO7GZ2Hq.eVgWJnU32fJk4k9oE1p2r0l.Gq98Y2.u
-- =======================================================================

USE `sea_db`;

-- 1. Insert Authentication Users
INSERT INTO `users` (`id`, `username`, `password`, `email`, `full_name`, `role`, `is_active`) VALUES
(1, 'admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8v9gY.c80Q7bV9g6oG7Y8xK9Z3P6uq', 'admin@matoshriengg.edu.in', 'Dr. G. K. Kharate (Principal)', 'ADMIN', TRUE),
(2, 'EMP101', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'a.deshmukh@matoshriengg.edu.in', 'Prof. A. Deshmukh', 'INVIGILATOR', TRUE),
(3, 'EMP102', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 's.patil@matoshriengg.edu.in', 'Prof. S. Patil', 'INVIGILATOR', TRUE),
(4, 'EMP103', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'r.joshi@matoshriengg.edu.in', 'Prof. R. Joshi', 'INVIGILATOR', TRUE),
(5, 'EMP104', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'v.kulkarni@matoshriengg.edu.in', 'Prof. V. Kulkarni', 'INVIGILATOR', TRUE),
(6, '2026CS001', '$2a$10$w095i3bI0zD9mO7GZ2Hq.eVgWJnU32fJk4k9oE1p2r0l.Gq98Y2.u', 'rahul.sharma@matoshriengg.edu.in', 'Rahul Sharma', 'STUDENT', TRUE),
(7, '2026CS002', '$2a$10$w095i3bI0zD9mO7GZ2Hq.eVgWJnU32fJk4k9oE1p2r0l.Gq98Y2.u', 'priya.patel@matoshriengg.edu.in', 'Priya Patel', 'STUDENT', TRUE),
(8, '2026IT001', '$2a$10$w095i3bI0zD9mO7GZ2Hq.eVgWJnU32fJk4k9oE1p2r0l.Gq98Y2.u', 'amit.kumar@matoshriengg.edu.in', 'Amit Kumar', 'STUDENT', TRUE),
(9, '2026AI001', '$2a$10$w095i3bI0zD9mO7GZ2Hq.eVgWJnU32fJk4k9oE1p2r0l.Gq98Y2.u', 'neha.gupta@matoshriengg.edu.in', 'Neha Gupta', 'STUDENT', TRUE),
(10, '2026ME001', '$2a$10$w095i3bI0zD9mO7GZ2Hq.eVgWJnU32fJk4k9oE1p2r0l.Gq98Y2.u', 'vikas.rao@matoshriengg.edu.in', 'Vikas Rao', 'STUDENT', TRUE);

-- 2. Insert Teachers
INSERT INTO `teachers` (`id`, `employee_id`, `name`, `department`, `designation`, `email`, `phone`, `is_supervisor`, `user_id`) VALUES
(1, 'EMP101', 'Prof. A. Deshmukh', 'Computer Engineering', 'Associate Professor', 'a.deshmukh@matoshriengg.edu.in', '9822011223', TRUE, 2),
(2, 'EMP102', 'Prof. S. Patil', 'IT', 'Assistant Professor', 's.patil@matoshriengg.edu.in', '9822011224', TRUE, 3),
(3, 'EMP103', 'Prof. R. Joshi', 'Artificial Intelligence & Data Science', 'Assistant Professor', 'r.joshi@matoshriengg.edu.in', '9822011225', TRUE, 4),
(4, 'EMP104', 'Prof. V. Kulkarni', 'Mechanical', 'Assistant Professor', 'v.kulkarni@matoshriengg.edu.in', '9822011226', TRUE, 5),
(5, 'EMP105', 'Prof. N. Shinde', 'Civil', 'Assistant Professor', 'n.shinde@matoshriengg.edu.in', '9822011227', TRUE, NULL);

-- 3. Insert Halls (Hall A, Hall B, Hall C, Hall D)
INSERT INTO `halls` (`id`, `hall_number`, `block`, `floor`, `total_rows`, `total_cols`, `is_active`) VALUES
(1, 'Hall A', 'Academic Block A', 'Ground', 5, 6, TRUE), -- Capacity: 30
(2, 'Hall B', 'Academic Block A', '1st', 5, 6, TRUE),    -- Capacity: 30
(3, 'Hall C', 'Academic Block B', '2nd', 6, 6, TRUE),    -- Capacity: 36
(4, 'Hall D', 'Academic Block B', '3rd', 6, 8, TRUE);    -- Capacity: 48

-- 4. Insert Exams
INSERT INTO `exams` (`id`, `exam_code`, `title`, `subject`, `exam_date`, `start_time`, `end_time`, `academic_year`, `semester`, `is_active`) VALUES
(1, 'EXAM-DS-2026', 'Data Structures & Algorithms Mid-Term', 'Data Structures', '2026-03-15', '10:00:00', '13:00:00', '2026-27', 4, TRUE),
(2, 'EXAM-AI-2026', 'Artificial Intelligence Foundations', 'AI Fundamentals', '2026-03-17', '10:00:00', '13:00:00', '2026-27', 4, TRUE),
(3, 'EXAM-TH-2026', 'Thermodynamics & Heat Transfer', 'Thermal Engineering', '2026-03-19', '14:00:00', '17:00:00', '2026-27', 4, TRUE);

-- 5. Insert Students (diverse branches for anti-malpractice interleaving)
INSERT INTO `students` (`id`, `roll_no`, `name`, `department`, `year`, `semester`, `email`, `phone`, `user_id`) VALUES
(1, '2026CS001', 'Rahul Sharma', 'Computer Engineering', 2, 4, 'rahul.sharma@matoshriengg.edu.in', '9876543210', 6),
(2, '2026CS002', 'Priya Patel', 'Computer Engineering', 2, 4, 'priya.patel@matoshriengg.edu.in', '9876543211', 7),
(3, '2026IT001', 'Amit Kumar', 'IT', 2, 4, 'amit.kumar@matoshriengg.edu.in', '9876543212', 8),
(4, '2026AI001', 'Neha Gupta', 'Artificial Intelligence & Data Science', 2, 4, 'neha.gupta@matoshriengg.edu.in', '9876543213', 9),
(5, '2026ME001', 'Vikas Rao', 'Mechanical', 2, 4, 'vikas.rao@matoshriengg.edu.in', '9876543214', 10),
(6, '2026CS003', 'Sneha Joshi', 'Computer Engineering', 2, 4, 'sneha.joshi@matoshriengg.edu.in', '9876543215', NULL),
(7, '2026IT002', 'Arjun Mehta', 'IT', 2, 4, 'arjun.mehta@matoshriengg.edu.in', '9876543216', NULL),
(8, '2026CV001', 'Riya Singh', 'Civil', 2, 4, 'riya.singh@matoshriengg.edu.in', '9876543217', NULL),
(9, '2026CS004', 'Karan Verma', 'Computer Engineering', 2, 4, 'karan.verma@matoshriengg.edu.in', '9876543218', NULL),
(10, '2026AI002', 'Pooja Sharma', 'Artificial Intelligence & Data Science', 2, 4, 'pooja.sharma@matoshriengg.edu.in', '9876543219', NULL),
(11, '2026ME002', 'Rohan Patil', 'Mechanical', 2, 4, 'rohan.patil@matoshriengg.edu.in', '9876543220', NULL),
(12, '2026IT003', 'Anjali Deshmukh', 'IT', 2, 4, 'anjali.d@matoshriengg.edu.in', '9876543221', NULL),
(13, '2026CS005', 'Tanmay Bhalerao', 'Computer Engineering', 2, 4, 'tanmay.b@matoshriengg.edu.in', '9876543222', NULL),
(14, '2026AI003', 'Aditya Kadam', 'Artificial Intelligence & Data Science', 2, 4, 'aditya.k@matoshriengg.edu.in', '9876543223', NULL),
(15, '2026CV002', 'Sayali More', 'Civil', 2, 4, 'sayali.m@matoshriengg.edu.in', '9876543224', NULL),
(16, '2026IT004', 'Pranav Chaudhari', 'IT', 2, 4, 'pranav.c@matoshriengg.edu.in', '9876543225', NULL),
(17, '2026CS006', 'Purva Anarthe', 'Computer Engineering', 2, 4, 'purva.a@matoshriengg.edu.in', '9876543226', NULL),
(18, '2026ME003', 'Prashant Dukare', 'Mechanical', 2, 4, 'prashant.d@matoshriengg.edu.in', '9876543227', NULL),
(19, '2026AI004', 'Krishna Darwante', 'Artificial Intelligence & Data Science', 2, 4, 'krishna.d@matoshriengg.edu.in', '9876543228', NULL),
(20, '2026IT005', 'Gaurav Jadhav', 'IT', 2, 4, 'gaurav.j@matoshriengg.edu.in', '9876543229', NULL);

-- 6. Insert Anti-Malpractice Seat Assignments for EXAM-DS-2026 (Exam ID: 1, Hall: Hall C)
-- Notice adjacent seats alternate between Computer Engineering, IT, AI & DS, Mechanical, Civil to prevent cheating
INSERT INTO `seat_assignments` (`id`, `student_id`, `exam_id`, `hall_id`, `supervisor_id`, `row_number`, `col_number`, `seat_number`, `qr_code_data`) VALUES
(1, 1, 1, 3, 1, 1, 1, 'R1-C1', '2026CS001|EXAM-DS-2026|R1-C1|b93ec817a022'),
(2, 3, 1, 3, 1, 1, 2, 'R1-C2', '2026IT001|EXAM-DS-2026|R1-C2|c41fe78a91b2'),
(3, 4, 1, 3, 1, 1, 3, 'R1-C3', '2026AI001|EXAM-DS-2026|R1-C3|e83da901c341'),
(4, 5, 1, 3, 1, 1, 4, 'R1-C4', '2026ME001|EXAM-DS-2026|R1-C4|f56eb123d567'),
(5, 8, 1, 3, 1, 1, 5, 'R1-C5', '2026CV001|EXAM-DS-2026|R1-C5|a12ef345b678'),
(6, 2, 1, 3, 1, 1, 6, 'R1-C6', '2026CS002|EXAM-DS-2026|R1-C6|789acb1234ef'),
(7, 7, 1, 3, 2, 1, 'R2-C1', '2026IT002|EXAM-DS-2026|R2-C1|987fed654321'),
(8, 10, 1, 3, 2, 2, 'R2-C2', '2026AI002|EXAM-DS-2026|R2-C2|321cba456789'),
(9, 11, 1, 3, 2, 3, 'R2-C3', '2026ME002|EXAM-DS-2026|R2-C3|654fed987123'),
(10, 15, 1, 3, 2, 4, 'R2-C4', '2026CV002|EXAM-DS-2026|R2-C4|147qwe258asd'),
(11, 6, 1, 3, 2, 5, 'R2-C5', '2026CS003|EXAM-DS-2026|R2-C5|369zxc951poi'),
(12, 12, 1, 3, 2, 6, 'R2-C6', '2026IT003|EXAM-DS-2026|R2-C6|753mnb852lkj');

-- 7. Insert QR Verification Audit Records
INSERT INTO `qr_verification` (`id`, `qr_payload`, `verification_hash`, `student_roll`, `exam_code`, `is_used`, `verified_at`, `device_ip`) VALUES
(1, '2026CS001|EXAM-DS-2026|R1-C1|b93ec817a022', 'b93ec817a022', '2026CS001', 'EXAM-DS-2026', TRUE, '2026-03-15 09:42:10', '192.168.1.105'),
(2, '2026IT001|EXAM-DS-2026|R1-C2|c41fe78a91b2', 'c41fe78a91b2', '2026IT001', 'EXAM-DS-2026', TRUE, '2026-03-15 09:44:35', '192.168.1.105'),
(3, '2026AI001|EXAM-DS-2026|R1-C3|e83da901c341', 'e83da901c341', '2026AI001', 'EXAM-DS-2026', FALSE, NULL, NULL),
(4, '2026ME001|EXAM-DS-2026|R1-C4|f56eb123d567', 'f56eb123d567', '2026ME001', 'EXAM-DS-2026', TRUE, '2026-03-15 09:46:12', '192.168.1.105');

-- 8. Insert Live Attendance Records
INSERT INTO `attendance` (`id`, `seat_assignment_id`, `student_id`, `exam_id`, `verified_by_teacher_id`, `status`, `scan_time`, `remarks`) VALUES
(1, 1, 1, 1, 1, 'PRESENT', '2026-03-15 09:42:10', 'Entry verified via contactless QR scan'),
(2, 2, 3, 1, 1, 'PRESENT', '2026-03-15 09:44:35', 'Entry verified via contactless QR scan'),
(3, 4, 5, 1, 1, 'PRESENT', '2026-03-15 09:46:12', 'Entry verified via contactless QR scan');
