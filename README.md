# Library Management API

A RESTful Library Management System developed using Java, Spring Boot, Spring Data JPA, Hibernate, and MySQL.

The application provides APIs to manage books, students, and book borrowing activities. It supports issuing and returning books, tracking overdue issues, validating requests, and handling application exceptions.

---

## 1. Technologies Used

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate ORM
- MySQL
- Maven
- Postman
- Git and GitHub
- Jakarta Bean Validation

---

## 2. Project Features

### Book Management
- Add new books.
- Retrieve all books.
- Retrieve a book by ID.
- Update book information.
- Delete books.
- Search books by category.
- Validate unique ISBN numbers.

### Student Management
- Register new students.
- Retrieve all students.
- Retrieve a student by ID.
- Update student information.
- Delete students.
- Validate unique email addresses.

### Book Issue Management
- Issue books to registered students.
- Retrieve all book issue records.
- Retrieve issues by student ID.
- Return issued books.
- Retrieve overdue book issues.
- Automatically decrease available copies when a book is issued.
- Automatically increase available copies when a book is returned.
- Restrict students to a maximum of three active book issues.

### Validation and Exception Handling
- Required-field validation.
- Email format validation.
- Duplicate ISBN validation.
- Duplicate student email validation.
- Custom exception classes.
- Global exception handling using `@RestControllerAdvice`.
- Appropriate HTTP response status codes.

---

## 3. Application Architecture

The project follows a layered architecture:

**Controller → Service → Repository → Database**

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests and API responses |
| Service | Implements business logic and application rules |
| Repository | Performs database operations using Spring Data JPA |
| Entity | Defines database entities and relationships |
| Exception | Handles application errors and custom exceptions |

---

## 4. Database Configuration

The application uses MySQL as its relational database.

### Create the Database

Open MySQL Workbench or your MySQL command line and execute:

```sql
CREATE DATABASE library_management;
```

### Configure Database Connection

Open:

`src/main/resources/application.properties`

Configure your local MySQL connection:

```properties
spring.application.name=LibraryManagementAPI

server.port=8186

spring.datasource.url=jdbc:mysql://localhost:3306/library_management
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace `YOUR_MYSQL_PASSWORD` with your local MySQL password.

**Security note:** Never commit your actual database password or other secrets to GitHub. Use a local configuration file or environment variables for private credentials.

---

## 5. Prerequisites

Before running the application, install:

- Java JDK 21
- Eclipse IDE or another Java IDE
- Apache Maven
- MySQL Server
- Postman
- Git

---

## 6. Installation and Setup

### Step 1: Clone the Repository

Open Git Bash or a terminal and execute:

```bash
git clone https://github.com/Disha-1012/LibraryManagementAPI.git
```

### Step 2: Open the Project

Import the cloned project into Eclipse:

1. Open Eclipse.
2. Select **File → Import**.
3. Select **Maven → Existing Maven Projects**.
4. Browse to the cloned `LibraryManagementAPI` folder.
5. Select the project and click **Finish**.

### Step 3: Create the MySQL Database

Execute:

```sql
CREATE DATABASE library_management;
```

### Step 4: Configure MySQL Credentials

Update `src/main/resources/application.properties` with your local MySQL username and password.

### Step 5: Build the Project

Open a terminal in the project root and run:

```bash
mvn clean install
```

### Step 6: Run the Application

Run the main Spring Boot application class:

`LibraryManagementApiApplication.java`

Alternatively, execute:

```bash
mvn spring-boot:run
```

The application runs at:

`http://localhost:8186`

---

## 7. API Documentation

### Base URL

```text
http://localhost:8186
```

All request and response bodies use JSON where applicable.

### 7.1 Book Management APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/books` | Add a new book |
| GET | `/api/books` | Retrieve all books |
| GET | `/api/books/{id}` | Retrieve a book by ID |
| PUT | `/api/books/{id}` | Update book details |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/category/{category}` | Retrieve books by category |

#### Example: Add Book

**POST** `/api/books`

Request body:

```json
{
  "title": "Java Programming",
  "author": "Herbert Schildt",
  "isbn": "9781260440232",
  "category": "Programming",
  "totalCopies": 10,
  "availableCopies": 10
}
```

### 7.2 Student Management APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/students` | Register a student |
| GET | `/api/students` | Retrieve all students |
| GET | `/api/students/{id}` | Retrieve a student by ID |
| PUT | `/api/students/{id}` | Update student details |
| DELETE | `/api/students/{id}` | Delete a student |

#### Example: Register Student

**POST** `/api/students`

Request body:

```json
{
  "studentName": "Rahul Das",
  "email": "rahul.das@example.com",
  "department": "Information Technology",
  "phoneNumber": "9876543210"
}
```

### 7.3 Book Issue Management APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/issues/{studentId}/{bookId}` | Issue a book |
| GET | `/api/issues` | Retrieve all issue records |
| GET | `/api/issues/student/{studentId}` | Retrieve issues by student |
| PUT | `/api/issues/return/{issueId}` | Return an issued book |
| GET | `/api/issues/overdue` | Retrieve overdue issues |

#### Example: Issue a Book

**POST** `/api/issues/1/1`

The student ID and book ID are supplied through the URL. No request body is required if the controller is implemented that way.

#### Example: Return a Book

**PUT** `/api/issues/return/1`

The issue ID is supplied through the URL. No request body is required if the controller is implemented that way.

---

## 8. Business Rules

The application implements the following business rules:

1. A book cannot be issued if no copies are available.
2. The student and book must exist before an issue can be created.
3. Available copies decrease by one when a book is issued.
4. Available copies increase by one when a book is returned.
5. A returned book cannot be returned again.
6. Each issue record contains an issue date and due date.
7. A student can have a maximum of three active book issues.
8. Book ISBN numbers must be unique.
9. Student email addresses must be unique.

The current issue service sets the due date to 14 days after the issue date.

---

## 9. HTTP Status Codes

| Status Code | Description |
|---|---|
| 200 OK | Request completed successfully |
| 201 Created | Resource created successfully, if returned by the controller |
| 204 No Content | Resource deleted successfully, if returned by the controller |
| 400 Bad Request | Invalid request or business-rule violation |
| 404 Not Found | Requested resource does not exist |
| 409 Conflict | Duplicate ISBN, duplicate email, or database constraint conflict |

---

## 10. API Testing with Postman

The REST APIs are tested using Postman.

Testing includes:

- Creating, retrieving, updating, and deleting books.
- Registering, retrieving, updating, and deleting students.
- Issuing and returning books.
- Retrieving overdue issues.
- Testing duplicate ISBN validation.
- Testing duplicate student email validation.
- Testing invalid request data.
- Testing book and student not-found scenarios.
- Testing issue requests when no copies are available.
- Testing the maximum three active issue restriction.
- Testing attempts to return an already returned book.

### Postman Collection

The exported Postman collection is stored in:

```text
postman/
└── LibraryManagementAPI-Testing.postman_collection.json
```

Import this collection into Postman to execute the API requests.

---

## 11. Project Structure

```text
LibraryManagementAPI
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.library
│   │   │       ├── controllers
│   │   │       ├── services
│   │   │       ├── repositories
│   │   │       ├── entities
│   │   │       ├── exceptions
│   │   │       └── LibraryManagementApiApplication.java
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│
├── postman
│   └── LibraryManagementAPI-Testing.postman_collection.json
│
├── screenshots
│   └── API testing screenshots
│
├── .gitignore
├── README.md
└── pom.xml
```

---

## 12. Git Branching Strategy

The project uses Git for version control and GitHub for repository hosting.

The following branches are used:

- `main` – Stable project code.
- `development` – Integrated development work.
- `feature/book-management` – Book Management feature development.

The Book Management feature branch is merged into the development branch, and the completed development branch is merged into main.

---

## 13. Author

Developed as an individual Spring Boot practical assignment.

**Project:** Library Management REST API

**Repository:** [LibraryManagementAPI](https://github.com/Disha-1012/LibraryManagementAPI)