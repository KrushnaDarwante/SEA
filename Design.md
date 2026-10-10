# UI/UX Design System & Layout Specifications
## SEA — Seat Exam Allotment System
**Design Framework:** Bootstrap 5.3 + Custom CSS3 Design Tokens  
**Typography:** Playfair Display (Display/Serif) & Inter (Body/Sans-Serif)  
**Supported Viewports:** Mobile (320px–480px), Tablet (768px–1024px), Desktop (1200px+)  

---

## 1. UI/UX Design Principles

1. **Simplicity & High Ergonomics:** The core administrative action — 1-Click Seating Allotment — is elevated to primary visual prominence with instantaneous feedback (<1s execution time).
2. **Role-Driven Workflows:** Tailored interfaces eliminate clutter:
   - **Admin:** Master management, batch allotment, live campus-wide monitoring.
   - **Student:** Single-purpose seat slip download and QR presentation without cognitive overload.
   - **Invigilator:** Full-screen camera scanner optimized for single-handed mobile operation in exam hall doorways.
3. **Contactless & Clear Feedback:** High-contrast QR codes, prominent seat badges (`R3-C5`), and dual visual status banners (Emerald checkmark for verified entry, Crimson cross for duplicate/tampered attempts).
4. **Paperless Print Architecture:** Built-in `@media print` style cascades that transform the student web view into an official institution hall ticket on standard A4 bounds.

---

## 2. Design Tokens & Color Palette

### 2.1. Color System
```css
:root {
    /* Primary Academic Blues */
    --primary: #0b3d91;         /* Academic Royal Blue */
    --primary-dark: #082c6b;    /* Deep Midnight Blue */
    --primary-light: #1e5bc6;   /* Vibrant Highlight */

    /* Accent & Heritage Golds */
    --accent: #c9a227;          /* CuriousParc Gold */
    --accent-light: #e6c458;    /* Muted Champagne */
    --accent-dark: #a8841a;     /* Deep Bronze */

    /* Neutral Slate Tones */
    --dark: #0f172a;            /* Slate 900 */
    --gray-700: #334155;        /* Slate 700 */
    --gray-500: #64748b;        /* Slate 500 */
    --gray-200: #e2e8f0;        /* Slate 200 Border */
    --bg-light: #f4f6fa;        /* Clean Neutral Background */

    /* Semantic Status */
    --success: #059669;         /* Verified Emerald */
    --danger: #dc2626;          /* Rejection / Duplicate Ruby */
    --warning: #d97706;         /* Pending Amber */
    --info: #0891b2;            /* Informational Cyan */
}
```

### 2.2. Typography Hierarchy
- **Brand & Headings:** `'Playfair Display', Georgia, serif`
  - $H1$: `2.2rem` (Hero numbers, hall names), Weight: 800
  - $H2$: `1.6rem` (Section banners), Weight: 700
  - $H3$: `1.2rem` (Card headers), Weight: 600
- **Body & Data:** `'Inter', -apple-system, BlinkMacSystemFont, sans-serif`
  - Body Text: `0.95rem`, Line-height: 1.6, Weight: 400
  - Table Data & Labels: `0.82rem`, Letter-spacing: 0.5px, Weight: 500/600
  - Micro Badges: `0.72rem`, Text-transform: uppercase, Letter-spacing: 1px, Weight: 700

---

## 3. Page Layouts & ASCII Wireframes

### 3.1. Admin Dashboard (`admin-dashboard.html`)
```text
+-------------------------------------------------------------------------------+
| [SEA Shield] SEA — Seat Exam Allotment            (User: Admin) [Logout]     |
+-------------------------------------------------------------------------------+
| [Sidebar]          | Page Header: Welcome back, Admin                          |
| - Students         | Metric Stat Cards:                                       |
| - Teachers         | [ 450 Students ] [ 25 Teachers ] [ 12 Halls ] [ 5 Exams ]|
| - Halls            |----------------------------------------------------------|
| - Exam Center      | Quick Actions:                                           |
| - Generate Seating | [ + Add Student ] [ Manage Teachers ] [ Manage Halls ]   |
| - Notices          | [ Add Exam      ] [ ⚡ 1-Click Generate Seating ]         |
+-------------------------------------------------------------------------------+
```

### 3.2. 1-Click Seating Allotment (`admin-seating.html`)
```text
+-------------------------------------------------------------------------------+
| Exam Selection: [ EXAM-DS-2026: Data Structures Mid-Term       v ]            |
| Active Halls:   [x] Hall A  [x] Hall C  [x] Hall D   |
| Options:        [x] Enforce Anti-Malpractice  [x] Randomize Within Branch     |
|                                                                               |
| [  ⚡ GENERATE SEATING ALLOTMENT (ONE CLICK)  ]                               |
|                                                                               |
| [✔ SUCCESS BANNER: 540 seats allotted across 18 halls in 12ms (Anti-Malp)]     |
|                                                                               |
| Seating Preview Matrix:                                                        |
| +-----------+-----------------+---------------------+--------+---------------+|
| | Roll No   | Student Name    | Hall                | Seat   | Supervisor    ||
| +-----------+-----------------+---------------------+--------+---------------+|
| | 2026CS001 | Rahul Sharma    | Hall C  | R1-C1  | Prof Deshmukh ||
| | 2026IT001 | Amit Kumar      | Hall C  | R1-C2  | Prof Deshmukh ||
| | 2026AI001 | Neha Gupta      | Hall C  | R1-C3  | Prof Deshmukh ||
| +-----------+-----------------+---------------------+--------+---------------+|
+-------------------------------------------------------------------------------+
```

### 3.3. Student Digital Seat Slip (`student-seat-details.html`)
```text
+-------------------------------------------------------------+
|               MATOSHRI COLLEGE OF ENGINEERING & RC                |
|             SEA Examination Control & Verification          |
+-------------------------------------------------------------+
| CANDIDATE:                  RAHUL SHARMA                    |
| Roll No: 2026CS001    | Department: Computer Science        |
| Year:    2nd Year     | Subject:    Data Structures         |
| Date:    15-Mar-2026  | Time Slot:  10:00 AM - 1:00 PM      |
+-------------------------------------------------------------+
|                      YOUR SEAT NUMBER                       |
|                          [ R1-C1 ]                          |
|           Hall: Block A – Hall C | Supervisor: Prof A     |
+-------------------------------------------------------------+
|                      [   QR CODE   ]                        |
|                      [   (HMAC)    ]                        |
|   Show this QR code at the hall entrance for verification   |
|                                                             |
| [Status Badge: Verified for Entry — Attendance Recorded]    |
+-------------------------------------------------------------+
|      [ 📄 Download Seat Slip (PDF) ]  [ 🔳 Download QR ]     |
+-------------------------------------------------------------+
```

### 3.4. Invigilator Mobile Scanner (`invigilator-scanner.html`)
```text
+-------------------------------------------------------------+
| SEA Invigilator Portal                    (Prof. A) [Logout]|
+-------------------------------------------------------------+
| Duty Details: Data Structures | Hall C | 15-Mar |
+-------------------------------------------------------------+
| [ LIVE CAMERA SCANNER VIEWPORT ]                            |
| +---------------------------------------------------------+ |
| |                        [ + ]                            | |
| |            Hold steady 15-25 cm from code               | |
| +---------------------------------------------------------+ |
|                                                             |
| Manual QR Simulator (Evaluation / Desktop Fallback):        |
| [ 2026CS001                     ] [ Verify QR ]             |
| Quick buttons: [2026CS001] [2026IT001] [2026AI001]          |
|                                                             |
| [✔ VERIFIED: 2026CS001 • Seat R1-C1 • Entry Logged]         |
+-------------------------------------------------------------+
```

---

## 4. Reusable UI Components

### 4.1. Navigation Brand Emblem
Custom SVG rendering an academic shield with gold and royal navy gradients, flanked by the institutional acronym and portal name.

### 4.2. Metric Stat Cards
Grid of responsive cards (`.stat-card`) with circular icon badges (Blue, Emerald, Gold, Cyan), featuring bold numerical counts and responsive micro-labels.

### 4.3. Anti-Malpractice Seat Badge
Pill-shaped badge (`.seat-badge`) with monospace font and high-contrast border displaying coordinates in `R{row}-C{col}` notation for unambiguous seat location.

### 4.4. Scan Status Result Cards
Dynamic card morphing between three contextual visual states:
- **Default/Active:** Cyan pulsing camera icon with "Ready to Scan".
- **Success:** Emerald background with checkmark icon and candidate roll/seat info.
- **Error / Rejection:** Crimson border with warning triangle, highlighting "Already Verified" or "Tampered QR Code".

---

## 5. Accessibility & Device Support

- **Tap Targets:** All buttons on mobile viewports enforce a minimum target size of $48\text{px} \times 48\text{px}$.
- **Contrast Ratios:** Text color combinations exceed WCAG AA standards (minimum contrast ratio of 4.5:1 for normal text).
- **Graceful Camera Degradation:** In environments where camera hardware is restricted, permission is denied, or running on localhost without HTTPS, the manual simulation bar automatically provides full functional testing capabilities.

