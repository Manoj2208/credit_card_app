# 💳 Credit Card Application

A Spring Boot RESTful service that allows users to apply for credit cards and view card details, with role-based access control using KeyCloak as Auth Server.

---

## 📌 Features

1. **Apply for a Credit Card**
    - Users can apply for a new credit card by providing their personal details and PAN.
    - Duplicate PAN entries are restricted to avoid multiple applications.

2. **Fetch Credit Card Details by User ID**
    - Accessible to both `USER` and `ADMIN` roles.
    - Requires valid JWT token for access.
    - Decrypts and returns card details securely.

3. **View All Credit Cards (Admin Only)**
    - Only users with `ADMIN` role can access this endpoint.
    - Returns all credit cards in the system with masked details.

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security with Oauth2 Resource Server**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Maven**
- **KeyCloak** (Auth server running in  docker engine)

---

## 🔐 Authentication and Roles

- Oauth2 with keycloak as AuthServer
- Two roles supported:
    - `USER`
    - `ADMIN`
- Role-based access to endpoints using `@PreAuthorize`.

---

## 📂 Folder Structure

```bash
com.mk.credit_card_app
│
├── controller/              # REST Controllers
├── dto/                    # Request/Response DTOs
├── entity/                 # JPA Entities
├── exception/              # Global & Custom Exception Handlers
├── repository/             # JPA Repositories
├── service/                # Business Logic Services
├── util/                   # Utility Classes (Encryption, Card Generator)
└── config/                 # Security & JWT Configuration (not shown above but expected)
