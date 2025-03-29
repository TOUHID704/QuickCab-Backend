# 🚖 **QuickCab - Backend**  

QuickCab is a ride-hailing service designed to handle everything from user authentication and ride management to payments and ratings. This backend, built using **Spring Boot** and **PostgreSQL**, provides APIs for drivers, riders, and admins.  

---

## 🟢 **API Documentation**  
👉 **Postman Documentation:** [QuickCab API Collection](https://documenter.getpostman.com/view/21564663/2sAYkHnxQV)  

👉 **Frontend Repository:** [QuickCab-Frontend](https://github.com/TOUHID704/QuickCab-Frontend)  

---

## 🏗️ **Project Structure**  
```
QuickCab-Backend/
└── uberApp
    ├── advices/          # Centralized exception handling
    ├── configs/          # App and Security Configurations
    ├── controllers/      # REST API Endpoints
    ├── dto/              # Data Transfer Objects
    ├── entities/         # JPA Entities
    ├── enums/            # Enums for Role and Ride Status
    ├── exceptions/       # Custom Exceptions
    ├── filters/          # JWT Filter for Security
    ├── repositories/     # Database Repositories
    ├── services/         # Service Interfaces
    ├── servicesImpl/     # Service Implementations
    ├── strategies/       # Business Strategies
    ├── strategiesImpl/   # Strategy Implementations
    └── utils/            # Utility Classes
```

---

## 🚀 **Key Features**  
1. **User Authentication & Authorization**  
   - JWT-based token authentication  
   - Role-based access control for Rider, Driver, and Admin  

2. **Ride Management**  
   - Riders can request, view, and cancel rides  
   - Drivers can accept, start, and end rides  
   - Real-time status updates  

3. **Wallet & Payment**  
   - Add funds, view balance, and track transactions  
   - Seamless payment processing  

4. **Ratings and Reviews**  
   - Riders and drivers can rate each other after rides  

5. **Email Notifications**  
   - Email notifications using SMTP  

---

## 🛠️ **Tech Stack**  

- **Backend Framework:** Spring Boot  
- **Security:** Spring Security, JWT  
- **Database:** PostgreSQL  
- **Build Tool:** Maven  
- **Object Mapping:** ModelMapper  
- **Validation:** Hibernate Validator  
- **Email Service:** Spring Mail (SMTP)  
- **API Documentation:** Postman  
- **Dependencies:** Lombok, Spring Data JPA, Spring Web  

---

## 📝 **Environment Setup**  

### ✅ **Prerequisites**  
- Java 17+  
- Maven 3.8+  
- PostgreSQL 14+  
- Postman (Optional for API testing)  

### ⚙️ **Configuration Overview**  
Create a configuration file `application.properties` or use environment variables to set the following:  

```properties
# Application Name
spring.application.name=YOUR_APP_NAME

# Database Configuration
spring.datasource.url=jdbc:postgresql://<DB_HOST>:<DB_PORT>/<DB_NAME>
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>

# Hibernate Settings
spring.jpa.hibernate.ddl-auto=update

# Server Port
server.port=8080

# JWT Secret Key
jwt.secretKey=<YOUR_SECRET_KEY>

# SMTP Configuration
spring.mail.username=<SMTP_USERNAME>
spring.mail.password=<SMTP_PASSWORD>
spring.mail.host=<SMTP_HOST>
spring.mail.port=<SMTP_PORT>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Swagger Documentation
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
```

👉 **Note:** Replace placeholders (e.g., `<DB_HOST>`, `<SMTP_USERNAME>`) with your actual values. Avoid hardcoding sensitive information—use environment variables in production.  

---

## 🎉 **Getting Started**  

1. **Clone the Repository**  
```bash
git clone https://github.com/yourusername/QuickCab-Backend.git
cd QuickCab-Backend
```

2. **Build the Project**  
```bash
mvn clean install
```

3. **Run the Application**  
```bash
mvn spring-boot:run
```

4. **Access the Application**  
- Application URL: `http://localhost:8080`  
- Database: PostgreSQL (using the specified schema)  


## 📮 **API Endpoints**  

### 🚪 **Authentication**  
- **Sign Up:** `POST /auth/signUp`  
- **Login:** `POST /auth/login`  
- **Refresh Token:** `POST /auth/refresh`  
- **Logout:** `POST /auth/logout`  

### 🏍️ **Ride Management (Rider)**  
- **Request Ride:** `POST /rider/ride/request`  
- **Cancel Ride Request:** `DELETE /rider/ride/cancel-request/{rideRequestId}`  
- **Cancel Ride:** `PATCH /rider/ride/cancel-ride/{rideId}`  
- **Get Ride History:** `GET /rider/rides/history`  
- **Get Active Rides:** `GET /rider/rides/active-rides`  

### 🚗 **Ride Management (Driver)**  
- **Accept Ride:** `POST /driver/ride/{rideRequestId}/accept`  
- **Start Ride:** `POST /driver/ride/{rideId}/start`  
- **End Ride:** `POST /driver/ride/{rideId}/end`  
- **Cancel Ride:** `POST /driver/ride/{rideId}/cancel`  

### 💳 **Wallet Management**  
- **Get Balance:** `GET /wallet/getBalance`  
- **Add Balance:** `PUT /wallet/addBalance/{amount}`  
- **Transaction History:** `GET /wallet/transactionHistory`  



## 🧪 **Running Tests**  

Run unit and integration tests:  
```bash
mvn test
```

---

## 🟢 **Using Postman for Testing**  
1. Import the Postman collection from the [documentation link](https://documenter.getpostman.com/view/21564663/2sAYkHnxQV).  
2. Set environment variables for `token` and `refreshToken` after authentication.  
3. Test the endpoints using the collection.

## 📸 **Screenshots**  

**IntelliJ IDEA:**  
![IntelliJ IDEA](https://github.com/user-attachments/assets/8e977025-5cca-4893-ac2b-39a158c3bce9)  

**Visual Studio Code:**  
![Visual Studio Code](https://github.com/user-attachments/assets/54fabcb3-4396-4120-94bf-089b0b967fa4)


**DBeaver:**  
![DBeaver](https://github.com/user-attachments/assets/9ca0dcc2-5e98-4aaa-8fdd-f7197f2353e1)  

**Postman:**  
![Postman](https://github.com/user-attachments/assets/d1916658-2902-4395-b3c2-394b37abfe7a)  

**Application UI:**  
![Application UI](https://github.com/user-attachments/assets/58e2703d-95b5-42a3-a71d-6d80146d9c30)  







## 📝 **License**  
This project is licensed under the MIT License.  
