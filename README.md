School Management System (SMS)

Streamline school operations with a centralized, web-based platform.

Table of Contents
1. Features
2. Technologies Used
3. Prerequisites
4. Installation and Setup
o Step 1: Clone the Repository
o Step 2: Set Up the Database
o Step 3: Build and Run the Project
5. API Documentation
6. Testing the Application
7. Database Schema
8. Future Improvements
9. Contributing
10. License
11. Contact

Features
The School Management System (SMS) provides the following key features:
• User Management: Create, update, and delete users with specific roles (Admin, Teacher, Student, Parent).
• Role-Based Access Control: Define permissions for each role to ensure secure access to system features.
• Grade Management: Manage classes or grade levels in the school.
• Student Management: Maintain student records, including enrollment, grades, and attendance.
• Parent Management: Link parents to their children and provide access to academic progress.
• Employee Management: Manage school staff, including teachers and administrators.
• Schedule Management: Create and manage timetables for classes and lessons.
• Lesson Management: Define lessons, assign teachers, and link them to subjects.
• Mark Management: Record and update student grades for lessons and subjects.
• Subject Management: Define and manage subjects taught in the school.

Technologies Used
• Backend: Java Spring Boot
• Database: H2 (in-memory) or PostgreSQL (production)
• API Documentation: Swagger UI and OpenAPI
• Frontend: Web-based interface (compatible with desktop and mobile browsers)

Prerequisites
Before running the project, ensure you have the following installed:
• Java Development Kit (JDK): Version 17 or higher.
• Maven: For building and managing dependencies.
• PostgreSQL (optional): For production database setup.
• IDE: IntelliJ IDEA, Eclipse, or any Java-supported IDE.
• Postman (optional): For testing REST APIs.
• Git: For cloning the repository.

Installation and Setup
Step 1: Clone the Repository
1. Open a terminal or command prompt.
2. Run the following command to clone the project repository:
git clone https://github.com/NargizaAlieva/school-app
Step 2: Set Up the Database
1. For H2 Database:
o No additional setup is required. The application uses an in-memory H2 database by default.
o Access the H2 console at: http://localhost:8080/h2
• JDBC URL: jdbc:h2:mem:school-app
• Username: sa
• Password: (leave blank)
2. For PostgreSQL:
o Create a new database named school_app.
o Update the database configuration in the application-postgres.properties file:
spring.datasource.url=jdbc:postgresql://localhost:5432/school-app
spring.datasource.username=postgres
spring.datasource.password=postgres
Step 3: Build and Run the Project
1. Using Maven:
o Run the following command to build and start the application:
mvn spring-boot:run
2. Using an IDE:
o Open the project in your IDE.
o Locate the main class: SchoolAppApplication.java.
o Run the application.

API Documentation
The system provides API documentation using Swagger UI and OpenAPI. Access the documentation at:
• Swagger UI: http://localhost:8080/swagger-ui.html
• OpenAPI JSON: http://localhost:8080/v3/api-docs

Testing the Application
1. Use Postman or any API testing tool to test the REST APIs.
2. Import the provided Postman collection (if available) to test all endpoints.
3. Alternatively, use the Swagger UI to test APIs interactively.

Database Schema
The database schema is designed around 10 core entities:
1. Users: Central table for all users (students, parents, teachers, admins).
2. Roles: Stores user roles (Admin, Teacher, Student, Parent).
3. Subjects: Stores subjects taught in the school.
4. Employees: Stores school staff (teachers, admins).
5. Parents: Stores parent/guardian information.
6. Grades: Represents classes/grade levels.
7. Students: Stores student information.
8. Schedules: Represents the timetable for classes.
9. Lessons: Stores individual lessons.
10. Marks: Stores student grades for lessons.
For detailed schema and relationships, refer to the Database Schema section in the SRS document.

Future Improvements
• Authorization and Authentication Enhancements:
o Implement Single Sign-On (SSO).
o Introduce passwordless authentication using magic links or device-based authentication.
o Implement OAuth 2.0 for secure authentication and authorization.
• Scalability:
o Enhance cloud-based deployment for better scalability.
o Introduce microservices architecture for modular expansion.
