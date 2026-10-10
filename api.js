/**
 * SEA — Frontend API Client
 * Seamless connection to Spring Boot REST backend with graceful offline fallback
 */

const API_CONFIG = {
    BASE_URL: window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1' 
        ? 'http://localhost:8080/api' 
        : 'https://sea-backend-render.onrender.com/api', // configured Render URL
    TIMEOUT_MS: 3000
};

const SeaApi = {
    getToken() {
        const user = JSON.parse(sessionStorage.getItem('user') || '{}');
        return user.token || '';
    },

    async request(endpoint, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };
        const token = this.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), API_CONFIG.TIMEOUT_MS);

        try {
            const res = await fetch(`${API_CONFIG.BASE_URL}${endpoint}`, {
                ...options,
                headers,
                signal: controller.signal
            });
            clearTimeout(timeoutId);
            const data = await res.json();
            return data;
        } catch (err) {
            clearTimeout(timeoutId);
            console.warn(`[SeaApi] Network request to ${endpoint} unavailable, falling back to local simulation.`, err);
            return null; // Signals fallback
        }
    },

    // 1. Authentication
    async login(username, password, role, department, year) {
        const res = await this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password, role, department, year })
        });
        if (res && res.success) {
            return res.data;
        }
        return null;
    },

    // 2. Dashboard Stats
    async getDashboardStats() {
        const res = await this.request('/dashboard/stats');
        if (res && res.success) return res.data;
        return {
            totalStudents: 450,
            totalTeachers: 25,
            totalHalls: 12,
            totalExams: 5,
            totalSeatsAllotted: 450,
            totalPresent: 382,
            attendancePercentage: 84.8
        };
    },

    // 3. One-Click Seating Allotment
    async generateSeating(examId, hallIds = []) {
        const res = await this.request('/allotment/generate', {
            method: 'POST',
            body: JSON.stringify({ examId, hallIds, enforceAntiMalpractice: true, randomizeWithinBranch: true })
        });
        return res;
    },

    // 4. Student Seat Details
    async getStudentSlip(rollNo) {
        const res = await this.request(`/allotment/student/${rollNo}`);
        if (res && res.success) return res.data;
        return null;
    },

    // 5. QR Code Entry Verification
    async verifyQR(qrPayload, examId = 1, teacherEmployeeId = 'EMP101') {
        const res = await this.request('/attendance/verify-qr', {
            method: 'POST',
            body: JSON.stringify({ qrPayload, examId, teacherEmployeeId, deviceMeta: navigator.userAgent })
        });
        return res;
    },

    // 6. Live Attendance Feed
    async getLiveAttendance(examId = 1) {
        const res = await this.request(`/attendance/live/${examId}`);
        if (res && res.success) return res.data;
        return null;
    }
};

window.SeaApi = SeaApi;
