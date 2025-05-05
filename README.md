**YouTube link finals:** https://youtu.be/Hxkp_Wfx_Z8?si=kyGHo0q0PuMmxtFc
**YouTube link midterms:** https://youtu.be/y0L0eaMjMvM?si=zmAX7Mp0PEJsY2SE 

# School Management System (SMS)

Streamline school operations with a centralized, web-based platform.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
  - [Step 1: Clone the Repository](#step-1-clone-the-repository)
  - [Step 2: Set Up the Database](#step-2-set-up-the-database)
  - [Step 3: Set Up JWT Authentication](#step-3-set-up-jwt-authentication)
  - [Step 4: Set Up OAuth2 Login](#step-4-set-up-oauth2-login)
  - [Step 5: Set Up Email-Based Two-Factor Verification](#step-5-set-up-email-based-two-factor-verification)
  - [Step 6: Build and Run the Project](#step-6-build-and-run-the-project)
- [API Documentation](#api-documentation)
- [Testing the Application](#testing-the-application)
- [Database Schema](#database-schema)
- [Future Improvements](#future-improvements)
- [Scalability](#scalability)

## Features
The **School Management System (SMS)** offers the following key features:
- **User Management**: Create, update, and delete users with specific roles (Admin, Teacher, Student, Parent).
- **Role-Based Access Control**: Securely define permissions for each role.
- **Grade Management**: Manage classes or grade levels within the school.
- **Student Management**: Maintain student records, including enrollment, grades, and attendance.
- **Parent Management**: Link parents to students and provide access to academic progress.
- **Employee Management**: Manage school staff, including teachers and administrators.
- **Schedule Management**: Create and manage class and lesson timetables.
- **Lesson Management**: Define lessons, assign teachers, and link them to subjects.
- **Mark Management**: Record and update student grades.
- **Subject Management**: Define and manage school subjects.

## Technologies Used
- **Backend**: Java Spring Boot, JWT Token, Spring Security
- **Database**: H2 (in-memory) or PostgreSQL (for production)
- **API Documentation**: Swagger UI & OpenAPI
- **Frontend**: Web-based interface (compatible with desktop and mobile browsers)

## Prerequisites
Before running the project, ensure you have:
- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: For dependency management and building
- **PostgreSQL (optional)**: For production database setup
- **IDE**: IntelliJ IDEA, Eclipse, or any Java-supported IDE
- **Postman (optional)**: For testing REST APIs
- **Git**: For cloning the repository

## Installation and Setup
### Step 1: Clone the Repository
```bash
git clone https://github.com/NargizaAlieva/school-app
```

### Step 2: Set Up the Database
#### Using H2 Database (Default)
No additional setup is required. The application runs with an in-memory H2 database.

Access the H2 console at: [http://localhost:8888/h2](http://localhost:8888/h2)
- **JDBC URL**: `jdbc:h2:mem:school-app`
- **Username**: `sa`
- **Password**: (leave blank)

#### Using PostgreSQL (Production Setup)
1. Create a new database named `school_app`.
2. Update the database configuration in `application-postgres.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/school-app
spring.datasource.username=postgres
spring.datasource.password=postgres
```
### Step 3: Set Up JWT Authentication

1. **Generate a Secret Key**  
   Use a JWT secret key generator (e.g., [https://www.allkeysgenerator.com](https://www.allkeysgenerator.com)) to generate a base64-encoded secret key.

2. **Create the Database**  
   Set up a new database named `school_app`.

3. **Update Configuration**  
   Add the following properties to your `application.properties` file:

   ```properties
   secret.key=your_base64_encoded_secret_key
   token.expiration=86400000                  # 1 day in milliseconds
   refresh-token.expiration=604800000         # 7 days in milliseconds
   verification-token.expiration=300000       # 5 minutes in milliseconds

### Step 4: Set Up OAuth2 Login
1. **Register Your App**
Register your application on each provider’s developer console:

- **Google Developer Console**
- **GitHub Developer Settings**
- **Facebook for Developers**

2. **Get Your Credentials**
Retrieve the Client ID and Client Secret from each platform.

3. **Update Configuration**
Add the credentials to your application.properties file

### Step 5: Set Up Email-Based Two-Factor Verification
1. **Generate an App Password**
For Gmail: Go to https://myaccount.google.com/apppasswords and generate an app password.
For other providers: Refer to their respective documentation.

2. **Configure Email Settings**
Add the following to your application.properties

### Step 6: Build and Run the Project
#### Using Maven
```bash
mvn spring-boot:run
```

#### Using an IDE
1. Open the project in your IDE.
2. Locate the main class: `SchoolAppApplication.java`.
3. Run the application.

## API Documentation
The system provides API documentation using **Swagger UI** and **OpenAPI**.
- **Swagger UI**: [http://localhost:8888/swagger-ui.html](http://localhost:8888/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8888/v3/api-docs](http://localhost:8888/v3/api-docs)

## Testing the Application
- Use **Postman** or any API testing tool to test REST APIs.
- Import the provided Postman collection (if available) to test all endpoints.
- Alternatively, use **Swagger UI** to test APIs interactively.

## Database Schema
The database schema is structured around **10 core entities**:
- **Users**: Central table for all users (students, parents, teachers, admins).
- **Roles**: Stores user roles (Admin, Teacher, Student, Parent).
- **Subjects**: Stores subjects taught in the school.
- **Employees**: Stores school staff (teachers, admins).
- **Parents**: Stores parent/guardian information.
- **Grades**: Represents classes/grade levels.
- **Students**: Stores student information.
- **Schedules**: Represents the timetable for classes.
- **Lessons**: Stores individual lessons.
- **Marks**: Stores student grades for lessons.
- **Tokens**: Stores user tokens.

For a detailed schema and entity relationships, refer to the **Database Schema** section in the SRS document.

## Future Improvements
### Authorization and Authentication Enhancements
- Implement **Single Sign-On (SSO)**.

### Scalability
- Enhance **cloud-based deployment** for better scalability.
- Introduce **microservices architecture** for modular expansion.
