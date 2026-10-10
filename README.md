# SEA — Seat Exam Allotment
### Automated Seat Allotment & QR-Enabled Entry Governance System
**State Innovation Challenge — CuriousParc 2026**  
*Har Seat. Har Student. Verified.*

---

## 📌 Executive Overview

**SEA (Seat Exam Allotment)** is an enterprise-grade full-stack examination seating and entry governance system developed to replace traditional, error-prone manual seating arrangements.

Manual seat planning takes **3–4 hours per examination session** and frequently places students from the same academic branch next to each other, creating significant risk of academic malpractice, copying, and proxy attendance. SEA reduces this preparation to a **single click (<1 second for 500+ candidates)** with a mathematically proven 2D anti-malpractice seating algorithm, tamper-proof QR seat slips, and contactless real-time gate entry governance.

```
Manual Seating (3-4 Hours) ➔ SEA 1-Click Allotment (<1s)
Paper Hall Tickets         ➔ Tamper-Proof Cryptographic QR Slips
Proxy Roll-Calls           ➔ Contactless Gate Scan with Single-Use Lock
Delayed Attendance Sheets  ➔ Live Real-Time Administrative Dashboard
```

---

## 🌟 Key Features

- **⚡ 1-Click Anti-Malpractice Seating:** Automatically allots 500+ candidates across arbitrary hall dimensions in under 600 ms.
- **🛡️ 0% Adjacent Branch Collision:** 2D row-offset algorithm ensures students of the same department never sit directly beside or in front of each other.
- **🔳 Cryptographic QR Seat Slips:** Generates tamper-proof QR codes signed with HMAC-SHA256, rendered in high-contrast Base64 PNG.
- **📱 Contactless Mobile Verification:** In-browser smartphone camera scanner (`html5-qrcode`) with zero app installation required.
- **🚫 Anti-Proxy Gate Security:** Rejects duplicate scans atomically with instant alert sound and timestamp.
- **📊 Real-Time Attendance Dashboard:** Live administrative oversight of campus-wide presence and attendance percentages.
- **📄 Paperless Slip Download:** Responsive web portal with instant printable PDF generation and vector QR image export.
- **🔐 Enterprise Spring Security:** BCrypt password hashing (10 rounds) and stateless JWT Bearer token authentication.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Frontend** | HTML5, CSS3, Bootstrap 5.3, JavaScript (ES6+), `html5-qrcode`, Canvas / SVG |
| **Backend** | Spring Boot 3.2.4, Java 17, Spring Data JPA, Hibernate 6, JJWT 0.11.5 |
| **Security** | Spring Security 6, BCrypt Password Encoder, HMAC-SHA256 Signature Verification |
| **Database** | MySQL 8.0 (InnoDB, Indexed relational schemas, Foreign Key cascades) |
| **QR Engine** | Google ZXing (`com.google.zxing:core:3.5.3` & `javase`) |
| **Build & Tools** | Maven 3.9+, Python 3.10+ (for stress testing & benchmarks), Git, Postman |

---

## 📁 Repository Structure

```text
SEA/
├── index.html                               # Unified Login Portal (Admin, Student, Invigilator)
├── admin-dashboard.html                     # Admin Command Center & Metric Overview
├── admin-seating.html                       # 1-Click Anti-Malpractice Seating Allotment
├── admin-students.html                      # Student Roster Management
├── admin-add-student.html                   # Student Registration Form
├── teacher-data.html                        # Invigilator & Faculty Supervisor Management
├── admin-halls.html                         # Hall Matrix Dimensions (Rows x Cols)
├── exam-center.html                         # Examination Scheduling & Centers
├── admin-notices.html                       # Campus Examination Notices
├── student-dashboard.html                   # Student Portal Dashboard
├── student-seat-details.html                # Digital Seat Slip, PDF Download & QR Code
├── student-notices.html                     # Student Announcements & Guidelines
├── invigilator-dashboard.html               # Supervisor Portal & Exam Duty Status
├── invigilator-scanner.html                 # Smartphone Camera QR Scanner & Manual Fallback
├── invigilator-attendance.html              # Live Hall Attendance Roster
├── invigilator-student-records.html         # Candidate Verification History
├── invigilator-overall-report.html          # Hall-wise Attendance Summary
├── api.js                                   # Unified REST Client with Offline Simulation Fallback
├── vercel.json                              # Zero-config Frontend Vercel Deployment
│
├── backend/                                 # Spring Boot (Java 17) REST API Backend
│   ├── pom.xml                              # Maven Dependencies (Spring Boot 3, ZXing, JWT, MySQL)
│   └── src/main/
│       ├── resources/
│       │   └── application.properties       # Datasource, JWT & Security Settings
│       └── java/com/sea/
│           ├── SeaApplication.java          # Spring Boot Application Entrypoint
│           ├── model/                       # JPA Relational Entities
│           │   ├── User.java                # Authentication & Roles (ADMIN, STUDENT, INVIGILATOR)
│           │   ├── Student.java             # Candidate Academic Profiles
│           │   ├── Teacher.java             # Invigilators & Supervisors
│           │   ├── Hall.java                # Hall Matrix Layout & Capacity
│           │   ├── Exam.java                # Scheduled Examinations
│           │   ├── SeatAssignment.java      # Seating Allotment Matrix
│           │   ├── Attendance.java          # QR-Verified Entry Logs
│           │   └── QRVerification.java      # Cryptographic Audit Log
│           ├── repository/                  # Spring Data JPA Repositories
│           ├── dto/                         # Request / Response Data Transfer Objects
│           ├── security/                    # SecurityConfig, JwtUtil, CustomUserDetailsService
│           ├── service/                     # Business Logic & Algorithms
│           │   ├── AntiMalpracticeSeatingService.java # 2D Anti-Malpractice Algorithm
│           │   ├── QRCodeService.java       # Google ZXing & HMAC-SHA256 Engine
│           │   ├── AttendanceService.java   # QR Entry & Anti-Proxy Validation
│           │   ├── AuthService.java         # Authentication & BCrypt Token Issuer
│           │   └── DashboardService.java    # Metric Counters
│           └── controller/                  # REST Controllers (/api/allotment, /api/attendance, etc.)
│
├── database/                                # MySQL 8.0 Relational Scripts
│   ├── schema.sql                           # Complete DDL Schema with Foreign Keys & Indexes
│   └── data.sql                             # Seed Sample Data (Admin, Faculty, Students, Halls, Exams)
│
├── python_utils/                            # Benchmarking & Data Generation Tools
│   ├── requirements.txt                     # Python Dependencies
│   ├── seed_data_generator.py               # 500+ Student Synthetic Dataset Generator
│   └── stress_test_allotment.py             # 540-Seat Anti-Malpractice Algorithmic Benchmark
│
├── PRD.md                                   # Product Requirements Document
├── ARCHITECTURE.md                          # 3-Tier System Architecture & Sequence Diagrams
├── Rules.md                                 # Engineering Standards, Code Style & Testing Rules
├── Phases.md                                # 5-Phase Implementation Roadmap (Design to AI Scale)
├── Design.md                                # UI/UX Design System, Color Tokens & ASCII Wireframes
├── Memory.md                                # State Management, MySQL Dictionary & AI Memory
├── DEPLOYMENT.md                            # Vercel & Render Production Deployment Guide
└── README.md                                # Project Documentation & Getting Started Guide
```

---

## 🚀 Quick Start Guide

### 1. Prerequisites
- **Java Development Kit (JDK):** Version 17+ installed (`java -version`).
- **MySQL Server:** Version 8.0+ running on port `3306`.
- **Modern Web Browser:** Chrome, Edge, Safari, or Firefox with camera permissions.

---

### 2. Database Setup (MySQL 8.0)

Open your MySQL terminal or Workbench and execute:
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/data.sql
```
This initializes the `sea_db` database, establishes all foreign key indexes, and inserts realistic seed records for administrators, teachers, halls, and students across CS, IT, AI, MECH, and CIVIL departments.

---

### 3. Run the Spring Boot Backend

Navigate to the `backend/` directory:
```bash
cd backend
mvn clean spring-boot:run
```
*(If Maven is not installed globally, run `./mvnw spring-boot:run` or launch `SeaApplication.java` inside IntelliJ IDEA / Eclipse / VS Code).*

The backend boots on **`http://localhost:8080`**.

Verify health:
```bash
curl http://localhost:8080/api/dashboard/stats
```

---

### 4. Launch the Frontend

The frontend is ready out of the box! You can open `index.html` directly in any web browser, or serve it with any static server:
```bash
# Using Python
python -m http.server 3000

# Using Node.js
npx serve .
```
Access the application at: **`http://localhost:3000/index.html`**.

> **Note on Resiliency:** If the backend is not booted, the frontend's built-in client (`api.js`) automatically provides local simulations so you can test all features (login, 1-click seating, QR download, camera scanner) without a running database!

---

## 🔑 Demo Access Credentials

| Role | Username / ID | Password | Department / Extra | Redirect Target |
|---|---|---|---|---|
| **Admin** | `admin` | `admin123` | *N/A* | `admin-dashboard.html` |
| **Invigilator** | `EMP101` | `invigilator123` | *N/A* | `invigilator-dashboard.html` |
| **Student** | `2026CS001` | *(Instant)* | CS / 2nd Year | `student-dashboard.html` |
| **Student 2** | `2026IT001` | *(Instant)* | IT / 2nd Year | `student-dashboard.html` |
| **Student 3** | `2026AI001` | *(Instant)* | AI / 2nd Year | `student-dashboard.html` |

---

## 🧪 Algorithmic Stress Testing (500+ Seats)

Run the automated Python benchmark tool to verify that the anti-malpractice algorithm allocates 540 seats in under 1 millisecond with zero adjacent collisions:
```bash
python python_utils/stress_test_allotment.py
```
**Benchmark Output:**
```text
============================================================
SEA ANTI-MALPRACTICE SEATING BENCHMARK RESULTS
============================================================
Total Students Allotted : 540
Total Halls Utilized    : 18
Execution Latency       : 0.57 ms
Direct Violations (Adj) : 0
Anti-Malpractice Success: 100.00%
============================================================
```

---

## 🌐 Cloud Deployment Links

- **Frontend Deployment Guide:** See [DEPLOYMENT.md](file:///c:/Users/ASUS/OneDrive/Desktop/New%20folder/SEA/SEA/DEPLOYMENT.md) for deploying to **Vercel** with custom routing and SSL.
- **Backend Deployment Guide:** See [DEPLOYMENT.md](file:///c:/Users/ASUS/OneDrive/Desktop/New%20folder/SEA/SEA/DEPLOYMENT.md) for deploying to **Render / Heroku** using Docker or Maven buildpacks.

---

## 👥 Project Team & Acknowledgments

- **Team:** Purva Anarthe, Pranav Chaudhari, Prashant Dukare, Krishna Darwante
- **Project Guide:** Dr. R. Gawande (Head of Department)
- **Institution:** Matoshri College of Engineering and Research Centre, Nashik (Academic Year 2026–27)
- **Event:** State Innovation Challenge — CuriousParc 2026
- **Contact:** examcell@matoshriengg.edu.in | +91 8237966738

---

## 📄 License & Intellectual Property

This project is developed for the CuriousParc 2026 State Innovation Challenge. All rights reserved © 2026 SEA Project Team.

