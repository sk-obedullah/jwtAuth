src/main/java/com/example/jwtauthsystem/
│
├── config/
│   ├── JwtConfig.java        # Holds JWT secret, expiration properties
│   ├── JwtUtils.java         # Utility for creating/validating JWT
│   └── SecurityConfig.java   # Spring Security filter chain & beans
│
├── controller/
│   └── AuthController.java   # REST endpoints for register & login
│
├── dto/
│   ├── AuthRequest.java      # Login request (username, password)
│   ├── AuthResponse.java     # Login response (JWT token)
│   └── UserDto.java          # Registration request (username, password, role)
│
├── entity/
│   └── User.java             # User entity with username, password, role
│
├── enums/
│   └── Role.java             # Enum for USER / ADMIN roles
│
├── repository/
│   └── UserRepository.java   # JPA repository for User
│
├── security/
│   ├── CustomUserDetails.java         # Wrapper for User (for Spring Security)
│   ├── CustomUserDetailsService.java  # Loads user from DB for authentication
│   └── JwtAuthenticationFilter.java   # Reads JWT from request, validates, sets authentication
│
├── service/
│   └── UserService.java      # Handles registration logic
│
└── JwtAuthSystemApplication.java      # Main Spring Boot class


2. Class-by-Class Explanation
Entity Layer

User.java
Represents users stored in DB. Contains id, username, password, and role.
Password is stored in BCrypt hashed form.

Role.java
Enum with USER and ADMIN.

DTO Layer

UserDto → Used for registration request.

AuthRequest → Used for login request (username + password).

AuthResponse → Sent back after login with the JWT token.

Repository Layer

UserRepository → Extends JpaRepository<User, Long> with a method findByUsername.

Service Layer

UserService

Registers new users.

Encodes password with BCryptPasswordEncoder.

Saves user in DB.

Prevents duplicate usernames.

Security Layer

CustomUserDetails → Implements UserDetails so that Spring Security can understand your User.

CustomUserDetailsService → Implements UserDetailsService. Loads a user from DB during login.

JwtConfig → Loads jwt.secret and jwt.expiration from application.yml.

JwtUtils

Generates JWT with username & role claims.

Validates JWT (expiration, signature, etc.).

Extracts username & role.

JwtAuthenticationFilter

Runs on every request.

Reads Authorization: Bearer <token> header.

Validates JWT using JwtUtils.

If valid → sets authenticated user into SecurityContext.

SecurityConfig

Disables CSRF (since you use JWT).

Configures session management as stateless.

Allows /api/auth/** endpoints (login, register) without authentication.

Secures other endpoints.

Adds JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter.

Provides PasswordEncoder bean.

Controller Layer

AuthController

POST /api/auth/register → Calls UserService to create user.

POST /api/auth/login → Authenticates user → Generates JWT → Returns AuthResponse.

Main Class

JwtAuthSystemApplication → Entry point.

3. Request Lifecycle Flow

Now, step by step:

A. User Registration

Client calls POST /api/auth/register with { username, password, role }.

AuthController → forwards request to UserService.

UserService:

Checks if username exists.

Encodes password with BCryptPasswordEncoder.

Saves new user to DB with role.

Response → “User registered successfully”.

B. User Login (JWT Issuance)

Client calls POST /api/auth/login with { username, password }.

AuthController calls AuthenticationManager.authenticate().

AuthenticationManager → uses CustomUserDetailsService to load user by username.

Password is compared (raw vs hashed) using BCryptPasswordEncoder.

If successful → JwtUtils.generateToken() creates JWT with:

Claims: username, role.

Signature: HMAC with secret.

Expiry: as per application.yml.

AuthResponse → { token: <JWT> } is sent back.

C. Accessing a Protected API with JWT

Client sends request with header:
Authorization: Bearer <JWT>

JwtAuthenticationFilter runs:

Extracts token from header.

Uses JwtUtils.validateToken() → checks expiry + signature.

If valid → extracts username.

Loads user from DB with CustomUserDetailsService.

Creates UsernamePasswordAuthenticationToken.

Sets it in SecurityContextHolder.

Request reaches controller.

If user has required role → allowed.

Else → 403 Forbidden.

D. Spring Security Validation

If token is missing/invalid → 401 Unauthorized.

If token is valid but role not permitted → 403 Forbidden.

If token valid + role permitted → request proceeds.

✅ In short:

Register → User saved in DB with hashed password.

Login → User validated, JWT issued.

Access API → JWT validated by filter, user authenticated, roles checked.
