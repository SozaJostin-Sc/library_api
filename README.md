# 📚 Library API

A RESTful API built with Spring Boot for managing a library system. It allows the administration of books, authors, categories, customers, loans, and returns.

---

## 🚀 Technologies Used

- ☕ Java 17
- ⚙️ Spring Boot 3.5.3
- 🗃️ Spring Data JPA
- 🐘 PostgreSQL
- 🌐 Spring Web
- 📑 Springdoc OpenAPI (Swagger UI)
- 📦 Maven
- ⚡ Caffeine Cache
- 📛 Hibernate Validator
- 🧰 Lombok

---

## 📦 Installation & Run

### 1. Clone the project

```bash
git clone https://github.com/SozaJostin-Sc/library_api.git
cd library-api
````

### 2. Configure the database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=your_url
spring.datasource.username=your_username
spring.datasource.password=your_password


Make sure PostgreSQL is running and that a database named `library` exists.

```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

---

## 🔍 API Documentation (Swagger UI)

Once the app is running, open your browser and go to:

```
http://localhost:8080/swagger-ui.html
```

You'll find all available endpoints and their documentation, automatically generated.

---

## 🧪 Main Endpoints

| Method | Path                     | Description             |
| ------ | ------------------------ | ----------------------- |
| GET    | `/api/books`             | Retrieve all books      |
| POST   | `/api/books`             | Create a new book       |
| PUT    | `/api/books/{id}`        | Update a book           |
| DELETE | `/api/books/{id}`        | Deactivate a book       |
| GET    | `/api/customers`         | List all customers      |
| POST   | `/api/customers`         | Register a new customer |
| GET    | `/api/authors`           | List all authors        |
| POST   | `/api/authors`           | Register a new author   |
| GET    | `/api/categories`        | List all categories     |
| POST   | `/api/categories`        | Create a new category   |
| POST   | `/api/loans`             | Register a book loan    |
| POST   | `/api/loans/return/{id}` | Register a book return  |

---

## 📁 Project Structure

```
src/main/java/com/personal/library/library_api/
├── controller         → REST controllers
├── dto                → Data Transfer Objects (DTOs)
│   ├── authors
│   ├── book
│   ├── category
│   ├── customer
│   └── loans
├── exception          → Exception handling per entity + global
│   └── GlobalExceptionHandler.java
├── model              → JPA entities
├── repository         → Data access layer
├── service            → Business logic
├── util               → Validators and helpers (per entity)
└── LibraryApiApplication.java
```

---

## 📝 Cache Configuration

The app uses **Caffeine** for efficient caching:

```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=100,expireAfterWrite=600s
```

---

## 📖 JSON Example for Creating a Book

```json
POST /api/books

{
  "title": "Clean Code",
  "isbn": "9780132350884",
  "publishedDate": "2008-08-01",
  "categoryId": 1,
  "authorId": 1
}
```

---

## ✅ Validation and Error Handling

* Input validation is handled with annotations like `@NotBlank`, `@Email`, `@Size`, etc.
* All errors are handled globally by `GlobalExceptionHandler`, returning clear and structured responses.

---

## 👤 Author

**Jostin Soza**
🔗 GitHub: [github.com/SozaJostin-Sc](https://github.com/SozaJostin-Sc)
📅 Year: 2025

---

## ⚠️ License

This project is licensed under the MIT License. Feel free to use, modify, and share it.


