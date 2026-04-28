# Business Analysis System — Backend

## Overview

This backend service powers a decision intelligence platform that analyzes business data from documents and generates actionable insights. It provides authentication, document processing, and AI-driven analysis using Gemini.

Users can upload past and target documents, compare them, and receive structured outputs such as insights, risks, and recommendations.

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Security (JWT Authentication)
* PostgreSQL
* Spring Data JPA
* REST APIs
* Gemini API (Google AI)
* Maven

---

## Features

* User registration and login with JWT authentication
* Secure API access using Bearer tokens
* Upload and compare PDF documents
* Extract and process business data
* AI-powered analysis using Gemini
* Structured response generation:

    * Insight
    * Risk
    * Recommendation
    * Explanation

---

## Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── model/
 ├── security/
 └── util/
```

---

## API Endpoints

### Authentication

POST /auth/register
Registers a new user

POST /auth/login
Returns JWT token

---

### Analysis

POST /apiv2/compare
Uploads two PDF files and returns analysis

Headers:

```
Authorization: Bearer <JWT_TOKEN>
```

Form Data:

```
past: file
target: file
```

Response:

```
{
  "insight": "...",
  "risk": "...",
  "recommendation": "...",
  "explanation": "..."
}
```

---

## Configuration

Update `application.yml`:

```
spring:
  application:
    name: system

  datasource:
    url: jdbc:postgresql://localhost:5432/YOUR_DB_NAME
    username: YOUR_DB_USER
    password: YOUR_DB_PASSWORD
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

gemini:
  api:
    key: YOUR_API_KEY
    url: https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
```

---

## Running Locally

### 1. Clone the repository

```
git clone https://github.com/YOUR_USERNAME/YOUR_BACKEND_REPO.git
cd YOUR_BACKEND_REPO
```

### 2. Configure PostgreSQL

* Create a database
* Update credentials in `application.yml`

### 3. Install dependencies

```
mvn clean install
```

### 4. Run the application

```
mvn spring-boot:run
```

Server runs at:

```
http://localhost:8080
```

---

## Security Notes

* JWT tokens are required for protected endpoints
* Do not commit API keys or database credentials
* Use environment variables in production

---

## Future Improvements

* Role-based access control
* Improved document parsing accuracy
* AI response validation and structuring
* Performance optimization for large files
* Dockerization and cloud deployment

---

## License

This project is for educational and demonstration purposes.
