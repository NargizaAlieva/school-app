YouTube link https://youtu.be/y0L0eaMjMvM?si=zmAX7Mp0PEJsY2SE 

# School Management System (SMS)

Streamline school operations with a centralized, web-based platform.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
  - [Step 1: Clone the Repository](#step-1-clone-the-repository)
  - [Step 2: Set Up the Database](#step-2-set-up-the-database)
  - [Step 3: Build and Run the Project](#step-3-build-and-run-the-project)
- [API Documentation](#api-documentation)
- [Testing the Application](#testing-the-application)
- [Database Schema](#database-schema)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

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
- **Backend**: Java Spring Boot
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

### Step 3: Build and Run the Project
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
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

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

For a detailed schema and entity relationships, refer to the **Database Schema** section in the SRS document.

## Future Improvements
### Authorization and Authentication Enhancements
- Implement **Single Sign-On (SSO)**.
- Introduce **passwordless authentication** (magic links, device-based authentication).
- Implement **OAuth 2.0** for secure authentication and authorization.

### Scalability
- Enhance **cloud-based deployment** for better scalability.
- Introduce **microservices architecture** for modular expansion.
