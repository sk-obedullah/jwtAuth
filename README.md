# JWT Auth System (Spring Boot)

A Spring Boot based **JWT Authentication & Authorization System** with user role management (`USER`, `ADMIN`), secured REST APIs, and log file management.

---

## 🚀 Tech Stack
- **Java 17+**
- **Spring Boot 3+**
- **Spring Security with JWT**
- **Spring Data JPA + Hibernate**
- **H2 (in-memory DB) / MySQL**
- **Lombok**
- **Validation API (Jakarta Validation)**
- **Logback (logging)**

---

## 📌 Features
- User registration & login
- JWT-based authentication & authorization
- Role-based access control (`USER`, `ADMIN`)
- Secure REST APIs
- Centralized exception handling
- Per-minute log rotation

---

## 📂 Project Structure
src/main/java/com/ssaa/auth
│── controller # REST Controllers (Auth, User)
│── dto # DTOs (RegisterRequest, LoginRequest, JwtResponse, etc.)
│── enums # Role Enum
│── exception # Global exception handlers
│── model # JPA Entities (User)
│── repository # Spring Data Repositories
│── security # JWT Filters, Configurations, Utils
│── service # Business logic (AuthService, UserService)
└── JwtAuthSystemApplication.java


 
 
