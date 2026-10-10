# Engineering Rules & Standards
## SEA — Seat Exam Allotment System
**Standard Version:** 1.0.0  
**Scope:** Backend (Java 17 / Spring Boot), Frontend (HTML5/CSS3/JS), Database (MySQL 8.0), Git & CI/CD  

---

## 1. Java & Spring Boot Coding Standards

### 1.1. Package Architecture & Separation of Concerns
All backend code resides under `com.sea` adhering strictly to layered enterprise boundaries:
- `com.sea.model`: JPA Entities only. Keep entities lightweight with minimal business logic.
- `com.sea.repository`: Spring Data JPA Repositories extending `JpaRepository<T, ID>`. Avoid raw JDBC.
- `com.sea.service`: Business orchestration, transactional boundaries (`@Transactional`), and algorithmic calculations.
- `com.sea.controller`: Thin REST controllers handling request mapping, validation (`@Valid`), and HTTP status mapping.
- `com.sea.dto`: Data Transfer Objects for client-server communication. Entities must **never** be directly returned from public controllers.
- `com.sea.security`: Security filters, JWT utilities, authentication providers, and CORS configuration.

### 1.2. Naming Conventions & Style
- **Classes:** PascalCase (e.g. `AntiMalpracticeSeatingService`, `AttendanceController`).
- **Methods & Variables:** camelCase (e.g. `verifyAndRecordQR`, `totalStudentsAllotted`).
- **Constants:** UPPER_SNAKE_CASE (e.g. `DEFAULT_QR_WIDTH`, `HMAC_ALGORITHM`).
- **Spring Annotations:** Prefer constructor injection via Lombok `@RequiredArgsConstructor` over `@Autowired` field injection.

### 1.3. Exception Handling & Error Envelopes
- Never suppress exceptions with empty `catch` blocks.
- Throw semantic unchecked exceptions (`IllegalArgumentException`, `IllegalStateException`, `SecurityException`).
- Standardize all API error responses into `ApiResponse<T>`:
```java
// Correct Standard:
return ResponseEntity.badRequest().body(ApiResponse.error("Student with roll " + roll + " not found."));
```

---

## 2. Frontend (JavaScript, HTML5 & CSS3) Standards

### 2.1. JavaScript (ES6)
- **Modern Syntax:** Exclusively use `const` and `let`; avoid `var`.
- **Asynchronous Operations:** Use `async/await` for network requests; avoid legacy callback nesting.
- **Fail-Safe Client Architecture:** Every network call through `api.js` must implement fallback handling to ensure client interfaces remain functional if the backend service is offline.
- **Session Security:** Clear sensitive credentials upon logout using `sessionStorage.clear()`.

### 2.2. HTML5 & CSS3
- **Responsive Layout:** Mobile-first approach using Bootstrap 5 grid (`container-fluid`, `col-12 col-md-6 col-lg-4`).
- **Design Tokens:** Use CSS variables defined in `:root` (e.g., `--primary: #0b3d91;`, `--accent: #c9a227;`).
- **Print Optimization:** All student seat slip views must declare `@media print` stylesheets hiding navigation bars, buttons, and footers while formatting the ticket to standard A4/Letter bounds.
- **Accessibility:** Use semantic elements (`<nav>`, `<main>`, `<aside>`, `<header>`) and descriptive `aria-label` attributes.

---

## 3. Database & SQL Standards (MySQL 8.0)

### 3.1. Schema Naming & Data Types
- **Table Names:** Lowercase, plural, snake_case (e.g., `seat_assignments`, `qr_verification`, `attendance`).
- **Column Names:** Lowercase, snake_case (e.g., `roll_no`, `exam_code`, `created_at`).
- **Primary Keys:** Auto-incrementing `BIGINT UNSIGNED PRIMARY KEY` named `id`.
- **Foreign Keys:** Named with prefix `fk_<source_table_abbrev>_<target_table>` (e.g., `fk_sa_student`).
- **Timestamps:** Every mutable table must define `created_at` and `updated_at` with automatic trigger timestamps.

### 3.2. Constraints & Data Integrity
- Enforce business logic in the schema where possible:
  - `uk_exam_student` ensures a student cannot be allocated twice for the same examination.
  - `uk_exam_hall_seat` ensures two students cannot physically occupy the identical seat in a hall.
  - `attendance.seat_assignment_id` must be `UNIQUE` to prevent duplicate or proxy scans.

---

## 4. Git Branching & Commit Conventions

### 4.1. Branching Strategy
- `master` / `main`: Production-ready, stable codebase.
- `develop`: Integration branch for active features.
- `feature/<feature-name>`: Feature development branches (e.g., `feature/qr-scanner-fallback`, `feature/anti-malpractice-algorithm`).
- `fix/<bug-name>`: Hotfixes and bug resolutions.

### 4.2. Conventional Commit Guidelines
Commit messages must adhere strictly to the Conventional Commits format:
```text
<type>(<scope>): <short imperative summary>

[optional body explaining rationale]
```
Allowed types:
- `feat`: A new user-facing feature or engine capability.
- `fix`: A bug fix.
- `docs`: Documentation updates (PRD, Architecture, README).
- `perf`: Performance improvements (e.g. optimizing seating algorithm from $O(N^2)$ to $O(N)$).
- `refactor`: Code restructurings without changing external behavior.
- `test`: Adding or updating test cases.
- `chore`: Tooling, dependencies (`pom.xml`), build scripts.

---

## 5. Testing & Quality Assurance Rules

1. **Anti-Malpractice Invariant Verification:** Every release candidate must pass the `stress_test_allotment.py` benchmark:
   - 500+ seat allocations processed in $<1000\text{ ms}$.
   - Direct adjacent branch collision count must strictly equal `0`.
2. **Duplicate QR Scan Verification:** Automated tests must verify that presenting a verified QR payload a second time returns HTTP 409 Conflict.
3. **Tampered QR Signature Test:** Altering any character in the roll number, exam code, or seat number without updating the HMAC must trigger HTTP 403 Forbidden.
4. **Clean Builds:** Maven build (`mvn clean package`) must compile with zero errors and zero deprecation warnings under Java 17.
