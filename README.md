# Hostel Complaint System - Backend

Spring Boot backend for the VIT Hostel Complaint Management System.

## Tech Stack
- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Cloudinary (file uploads)

## Setup

1. **Create PostgreSQL Database**
   ```sql
   CREATE DATABASE hostel_complaint;
   ```

2. **Configure Environment**
   - Copy `.env.example` to `.env`
   - Update with your credentials:
     - Database connection
     - Cloudinary credentials (get from cloudinary.com)

3. **Run Application**
   ```bash
   ./mvnw spring-boot:run
   ```

## API Endpoints

### Public
- `POST /api/auth/register` - Student registration
- `POST /api/auth/login` - Login
- `GET /api/hostels` - Get all hostels
- `GET /api/hostels?gender=MALE` - Filter by gender

### Student (Requires Auth)
- `POST /api/student/complaints` - Create complaint
- `GET /api/student/complaints` - Get my complaints
- `GET /api/student/complaints/{id}` - Get complaint details

### Admin (Requires Auth)
- `GET /api/admin/complaints` - Get all block complaints
- `GET /api/admin/complaints/{id}` - Get complaint details
- `PUT /api/admin/complaints/{id}/status` - Update status
- `GET /api/admin/dashboard/stats` - Dashboard statistics
- `GET /api/admin/dashboard/recent` - Recent complaints

### File Upload
- `POST /api/upload` - Upload image

## Default Admin Credentials
- Email: `{block}@vitadmin.ac.in` (e.g., `mhkblock@vitadmin.ac.in`)
- Password: `admin123`

**⚠️ Change these passwords in production!**
