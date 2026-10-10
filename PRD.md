# Product Requirements Document (PRD)
## SEA — Seat Exam Allotment & QR-Enabled Entry Governance System
**Project Code:** SEA-CP2026  
**Document Version:** 1.0.0  
**Target Milestone:** CuriousParc 2026 State Innovation Challenge MVP & Production Deployment  
**Status:** Approved & Implemented  

---

## 1. Executive Summary & Objective

Manual examination seat allotment in collegiate and university environments is labor-intensive, taking between **3 to 4 hours per examination session** for batches of 500+ students. Furthermore, manual arrangements frequently place students from the same academic department or branch in adjacent seats, drastically elevating the risk of academic malpractice, copying, and proxy attendance.

**SEA (Seat Exam Allotment)** is a unified full-stack examination governance platform engineered to:
1. **Automate seat allocation with a single click** for 500+ students across multiple examination halls in under one second (<1000 ms).
2. **Eliminate cheating and malpractice** via an algorithmic 2D row-offset anti-malpractice seating engine that strictly separates students of identical branches horizontally and vertically.
3. **Transition to 100% paperless digital seat slips** featuring HMAC-SHA256 cryptographically signed QR codes.
4. **Enforce contactless, tamper-proof entry governance** using smartphone cameras and web-based QR scanning, updating live administrative dashboards in real time.
5. **Abolish proxy attendance** through atomic single-use scan validation.

---

## 2. Problem Statement & Market Gaps

| Traditional Manual Exam Workflow | SEA Digital Governance Workflow |
|---|---|
| **3–4 Hours Seating Preparation:** Staff manually map students to benches using spreadsheets and chalk boards. | **Sub-Second 1-Click Execution:** Automated matrix allotment processes 500+ seats across multiple halls in <600 ms. |
| **High Malpractice Risk:** Students of the same branch and subject often sit directly next to or behind each other. | **0% Adjacent Branch Collision:** Algorithmic interleaving ensures adjacent desks belong to distinct branches. |
| **Paper Wastage & Distribution Bottlenecks:** Physical hall tickets printed, transported, signed, and collected. | **Paperless Digital Seat Slips:** Responsive web portal with immediate PDF download and PNG QR export. |
| **Vulnerable to Proxy Attendance:** Manual roll-call signatures inside noisy halls allow proxy signing and impersonation. | **Tamper-Proof QR Gate Entry:** Contactless verification at the hall door with instant duplicate scan rejection. |
| **Zero Administrative Real-Time Visibility:** Attendance statistics compiled hours after the exam finishes. | **Live Real-Time Dashboard:** Instant live feed of present vs. absent students per hall and department. |

---

## 3. Target User Personas & Workflows

### 3.1. Admin / Examination Cell Officer
- **Role:** Chief exam controller managing master records, schedules, and allotment.
- **Key Needs:**
  - Fast onboarding of student rosters, invigilators, and hall dimensional layouts.
  - 1-Click execution of anti-malpractice seating.
  - Live oversight of campus-wide entry verification and attendance percentage.
- **Workflow:**
  1. Log in via `/index.html` (Role: Admin).
  2. Review enrolled students, available halls, and scheduled examinations.
  3. Navigate to `/admin-seating.html`, select target exam, click **"Generate Seating"**.
  4. View generated hall-wise seating grid and supervisor assignments.
  5. Monitor live verification progress on the Admin Dashboard during exam morning.

### 3.2. Student Candidate
- **Role:** Examinee requiring timely access to exam location, seat number, and entry pass.
- **Key Needs:**
  - Hassle-free login without cumbersome password recovery during exam stress.
  - Clear presentation of seat label (`R{row}-C{col}`), hall number, block, and supervisor.
  - Offline-accessible digital seat slip with embedded QR code.
- **Workflow:**
  1. Enter Roll Number (e.g. `2026CS001`), Department, and Year on `/index.html`.
  2. Access `/student-seat-details.html`.
  3. View designated seat number (e.g. `R1-C1`) and hall location (`Hall C`).
  4. Click **"Download Seat Slip"** (formatted print/PDF) or **"Download QR Code"** (PNG).
  5. Present QR code on mobile screen or printout to the invigilator at the entrance.

### 3.3. Invigilator / Hall Supervisor
- **Role:** Faculty member assigned to an exam hall to verify entries and maintain integrity.
- **Key Needs:**
  - Lightweight scanner requiring no specialized handheld barcode terminal.
  - Instant visual and auditory feedback on verification status (Success, Invalid, Duplicate).
  - Rapid manual fallback option for scratched screens or camera malfunctions.
- **Workflow:**
  1. Log in with faculty Employee ID (`EMP101`) on `/index.html`.
  2. Open `/invigilator-scanner.html` on smartphone browser or laptop.
  3. Point camera at student's QR code.
  4. System verifies HMAC signature, marks student `PRESENT`, and locks the QR code.
  5. If an already verified student attempts a second entry, system sounds an immediate **"Already Verified"** warning.

---

## 4. Detailed Feature Specifications

### 4.1. One-Click Anti-Malpractice Seating Engine
- **Capacity:** Supports 500+ candidates distributed across arbitrary hall matrix dimensions ($R \times C$).
- **Algorithmic Invariant:** No student sharing the same academic department shall be seated horizontally adjacent (same row, $C \pm 1$) or vertically adjacent (same column, $R \pm 1$) whenever multiple branches are participating.
- **Implementation:** Interleaved multi-branch round-robin queue with 2D row-offset modulo shifting ($O(N)$ computational complexity).
- **Execution Performance:** Maximum execution threshold $\le 1000\text{ ms}$ for $N=1000$ students.

### 4.2. Cryptographic QR Generation & Integrity Verification
- **Payload Format:** `ROLL_NUMBER|EXAM_CODE|SEAT_NUMBER|HMAC_SIGNATURE`
  - Example: `2026CS001|EXAM-DS-2026|R1-C1|b93ec817a022`
- **Signing Algorithm:** HMAC-SHA256 truncated to 12 hex characters using server-side master salt (`sea.qr.secret-salt`).
- **Rendering:** Google ZXing library producing high-contrast 220x220 PNG Base64 data URLs.
- **Anti-Counterfeiting:** Scanning a forged or manually altered QR code triggers HTTP 403 Forbidden with alert `"Tampered QR Code detected"`.

### 4.3. Anti-Proxy Gate Verification
- **Single-Use Enforcement:** The `attendance` table enforces a `UNIQUE(seat_assignment_id)` constraint.
- **Duplicate Detection:** If a QR code is presented a second time, the system returns HTTP 409 Conflict with timestamp of original entry and rejects admittance.

### 4.4. Live Real-Time Attendance Governance
- **Polling / WebSocket Readiness:** REST endpoint `GET /api/attendance/live/{examId}` provides live verified candidate roll, seat, timestamp, and verifying supervisor.
- **Metrics Calculated:** Total Allotted, Total Present, Total Absent, Attendance Rate (%).

---

## 5. Non-Functional Requirements (NFRs)

| Metric | Target Specification |
|---|---|
| **Response Latency** | Seating allotment generation $\le 1000\text{ ms}$; QR verification $\le 100\text{ ms}$. |
| **Scalability** | Single instance supports 5,000+ concurrent students checking seat slips on exam morning. |
| **Availability** | 99.9% uptime during active academic examination windows. |
| **Security Standards** | Passwords hashed with BCrypt (10 rounds); Stateless JWT Bearer authentication; Role-Based Access Control (RBAC). |
| **Device Responsiveness** | Seamless rendering on Mobile (320px–480px), Tablet (768px–1024px), and Desktop (1200px+). |
| **Browser Compatibility** | Chrome 90+, Safari 14+, Edge 90+, Firefox 88+ with camera permission APIs. |

---

## 6. Success Metrics & Key Performance Indicators (KPIs)

1. **80%+ Reduction in Administrative Preparation Time:** From 3–4 hours of manual drafting down to a single click (<1 second).
2. **Zero Adjacent Branch Collisions:** 100% adherence to same-branch separation in mixed-stream exams.
3. **100% QR-Verified Audited Entry:** Complete elimination of manual paper signature logs.
4. **Zero Proxy Attendance Incidents:** Atomic single-use validation prevents reuse of credentials.
5. **Significant Paper Reduction:** Saving an average of 1,000+ sheets of paper per major examination session.

