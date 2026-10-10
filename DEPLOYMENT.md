# Production Deployment Guide
## SEA — Seat Exam Allotment System
**Target Environments:** Vercel (Frontend Client) | Render / Heroku (Spring Boot REST Backend) | Managed MySQL 8.0  

---

## 1. Cloud Architecture Overview

SEA is designed for cloud-native decoupled deployment:
- **Frontend CDN:** Hosted on **Vercel** with global edge caching, instant SSL, and zero cold starts.
- **Backend Application:** Hosted on **Render** (or Heroku) as a containerized Java 17 web service with auto-scaling.
- **Database:** Hosted on **Aiven, Railway, or AWS RDS** running MySQL 8.0 with automated backups.

```mermaid
flowchart LR
    Browser[Client Web Browsers] -->|HTTPS| Vercel[Vercel Global CDN (Frontend)]
    Vercel -->|REST JSON API| Render[Render Web Service (Spring Boot 3 / Java 17)]
    Render -->|JDBC SSL Port 3306| DB[(Managed MySQL 8.0 Database)]
```

---

## 2. Database Deployment (MySQL 8.0)

Before deploying the backend, provision a managed MySQL 8.0 database:

### Option A: Free/Managed Cloud MySQL on Aiven / Railway / PlanetScale
1. Sign up at [Aiven.io](https://aiven.io) or [Railway.app](https://railway.app).
2. Create a new **MySQL 8.0** service (Region: Asia South or closest to your campus).
3. Obtain the connection URI:
   ```text
   jdbc:mysql://<HOST>:<PORT>/sea_db?sslMode=REQUIRED
   ```
4. Connect via MySQL Workbench or command line and run the DDL & seed scripts:
   ```bash
   mysql -h <HOST> -P <PORT> -u <USER> -p < database/schema.sql
   mysql -h <HOST> -P <PORT> -u <USER> -p < database/data.sql
   ```

---

## 3. Backend Deployment on Render

### Step 3.1. Prepare the Backend Files
The repository includes a production-ready `Dockerfile` and Maven build setup in `backend/`.

### Step 3.2. Deploy via Render Dashboard
1. Log in to [Render.com](https://render.com) and click **"New +" ➔ "Web Service"**.
2. Connect your GitHub repository: `https://github.com/<your-username>/SEA`.
3. In the Web Service configuration:
   - **Name:** `sea-backend`
   - **Root Directory:** `backend`
   - **Environment:** `Docker` (or `Java`)
   - **Instance Type:** Free or Starter
4. Add the following **Environment Variables**:

| Variable Name | Example Value / Purpose |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://<DB_HOST>:<DB_PORT>/sea_db?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC` |
| `SPRING_DATASOURCE_USERNAME` | `<DB_USER>` |
| `SPRING_DATASOURCE_PASSWORD` | `<DB_PASSWORD>` |
| `JWT_SECRET` | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` |
| `QR_SALT` | `MATOSHRI-MCERC-2026-PRODUCTION-SALT` |
| `PORT` | `8080` |

5. Click **"Deploy Web Service"**.
6. Render will automatically build the Maven artifact and start the service. Note your backend URL (e.g. `https://sea-backend.onrender.com`).

---

## 4. Alternative Backend Deployment on Heroku

If deploying on Heroku:
1. Ensure the Heroku CLI is installed (`heroku --version`).
2. Log in and create the app:
   ```bash
   heroku login
   heroku create sea-backend-app
   ```
3. Set the buildpack:
   ```bash
   heroku buildpacks:set heroku/java
   ```
4. Set environment configuration:
   ```bash
   heroku config:set SPRING_DATASOURCE_URL="jdbc:mysql://<HOST>:<PORT>/sea_db?useSSL=true"
   heroku config:set SPRING_DATASOURCE_USERNAME="<USER>"
   heroku config:set SPRING_DATASOURCE_PASSWORD="<PASSWORD>"
   heroku config:set JWT_SECRET="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
   ```
5. Deploy:
   ```bash
   git subtree push --prefix backend heroku main
   ```

---

## 5. Frontend Deployment on Vercel

The frontend is a static web application configured with `vercel.json` for URL routing and security headers.

### Step 5.1. Update the API Endpoint in `api.js`
Open `api.js` and set your live Render backend URL:
```javascript
const API_CONFIG = {
    BASE_URL: window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
        ? 'http://localhost:8080/api'
        : 'https://sea-backend.onrender.com/api', // your live Render URL
    TIMEOUT_MS: 4000
};
```

### Step 5.2. Deploy via Vercel CLI
1. Install the Vercel CLI globally (if not already installed):
   ```bash
   npm install -g vercel
   ```
2. In the root `SEA` directory, execute:
   ```bash
   vercel --prod
   ```
3. Follow the interactive prompts:
   - *Set up and deploy?* ➔ `Y`
   - *Which scope?* ➔ `<Your Account>`
   - *Link to existing project?* ➔ `N`
   - *What's your project's name?* ➔ `sea-exam-allotment`
   - *In which directory is your code located?* ➔ `./`
4. Vercel will output your production URL:
   ```text
   ✅ Production: https://sea-exam-allotment.vercel.app [Copied to clipboard]
   ```

### Step 5.3. Deploy via Vercel GitHub Dashboard (No-CLI Alternative)
1. Push your repository to GitHub.
2. Go to [vercel.com/new](https://vercel.com/new) and import the repository.
3. Select **Framework Preset:** `Other`.
4. Click **Deploy**. Vercel will publish the site globally in under 20 seconds.

---

## 6. Post-Deployment Verification Checklist

1. [ ] **Frontend Loads:** Visit `https://your-app.vercel.app/index.html`. Verify stylesheet, fonts, and login forms load properly.
2. [ ] **Backend Healthcheck:** Visit `https://your-backend.onrender.com/api/dashboard/stats`. Verify HTTP 200 with JSON metric payload.
3. [ ] **Admin Seating Allotment:**
   - Log in with `admin` / `admin123`.
   - Go to `admin-seating.html`.
   - Click "Generate Seating". Confirm that 500+ seat allocations are generated and displayed in the table.
4. [ ] **Student Slip & QR Download:**
   - Log in with `2026CS001`.
   - Click "Download Seat Slip" to verify print preview formatting.
   - Click "Download QR Code" to confirm PNG file download.
5. [ ] **Invigilator Gate Verification:**
   - Open `invigilator-scanner.html` on a smartphone.
   - Scan student QR code (or use the built-in manual simulator for `2026CS001`).
   - Verify green confirmation badge appears and student status transitions to `PRESENT`.
   - Re-scan identical QR code to verify that the duplicate warning ("Already Verified") triggers.

