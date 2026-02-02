# 🍃 Spring Blog Project (Monolithic Architecture)

A robust, full-stack monolithic blog application built with **Spring Boot 3**. This project demonstrates the core principles of server-side rendering, secure authentication, and database management in a Java environment.

## 🛠 Tech Stack
* **Java 17** 
* **Spring Boot 3.x**
* **Spring Security** (Role-based Access Control)
* **Spring Data JPA** (Hibernate)
* **Thymeleaf** (Server-side Template Engine)
* **H2 Database** (Development) / **MySQL** (Production ready)
* **Maven** (Dependency Management)

## 🔑 Key Features
* **User Authentication:** Secure Login/Logout and Registration flows.
* **Role-Based Authorization:** Separate views and permissions for Admin and User roles.
* **Blog Management:** Full CRUD (Create, Read, Update, Delete) operations for blog posts.
* **Security:** Protection against common vulnerabilities (CSRF, XSS) using Spring Security.

## 🚀 Getting Started

### Prerequisites
* JDK 17 or higher
* Maven 3.6+
* Your favorite IDE (IntelliJ IDEA recommended)

### Installation & Run
1. Clone the repository:
   ```bash
   git clone git@github.com:arifmertmisir/SpringBlogProject.git
2. Open the project in your IDE.
3. Let Maven download the dependencies. 
4. Run the application: 
   ```bash 
   ./mvnw spring-boot:run
5. Access the app at: http://localhost:8080

## 🛤 Roadmap
* Initial Monolithic Setup
* Spring Security Integration
* ransition to DTO (Data Transfer Object) pattern
* Implementation of REST APIs
* Decoupling Frontend (Plan: React or Angular integration)

Developed by [Arif Mert Misir](https://github.com/arifmertmisir)

