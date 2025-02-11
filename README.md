# Leave Management PRJ301

## Overview

Leave Management PRJ301 là hệ thống quản lý nghỉ phép cho nhân viên, hỗ trợ đăng ký, phê duyệt và theo dõi lịch nghỉ phép.

## Tech Stack

### Backend

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.2
- **Database**: Microsoft SQL Server
- **Build Tool**: Maven
- **Security**: Spring Security, JWT
- **API**: RESTful API

### Frontend

- **Library**: React (Vite)
- **State Management**: Redux Toolkit
- **UI Framework**: Material-UI, bootstrap 5, Ant Design
- **Routing**: react-router-dom
- **HTTP Client**: Axios

## Project Structure

### Backend (`/backend`)backend/

```
backend/
├── src
│   ├── main
│   │   ├── java
│   │   │   └── quyet
│   │   │       └── leavemanagement
│   │   │           └── backend
│   │   │               ├── configuration       # Cấu hình chung (CORS, Security, JWT, etc.)
│   │   │               ├── controller          # Xử lý API request
│   │   │               ├── dto                 # Data Transfer Objects (DTO)
│   │   │               │   ├── request         # DTO cho request từ client
│   │   │               │   └── response        # DTO cho response gửi về client
│   │   │               ├── entity              # Các class ánh xạ bảng trong DB
│   │   │               ├── exception           # Xử lý ngoại lệ
│   │   │               ├── repository          # Spring Data JPA repositories
│   │   │               ├── scheduler           # Xử lý tác vụ theo lịch trình
│   │   │               └── service             # Business Logic
│   │   └── resources
│   │       ├── static                          # Chứa file static
│   │       └── templates                       # Chứa file template
│   └── test
│       └── java
│           └── quyet
│               └── leavemanagement
│                   └── backend

```

### Frontend (`/frontend`)

```

```

## Installation & Setup

### Backend

1. Cài đặt Java 21 và Maven
2. Cấu hình database trong `application.properties`
3. Chạy ứng dụng bằng lệnh:
   ```sh
   mvn spring-boot:run
   ```

### Frontend

1. Cài đặt Node.js (>= 18)
2. Cài đặt dependencies:
   ```sh
   cd frontend
   yarn install
   ```
3. Chạy ứng dụng:
   ```sh
   yarn dev
   ```

## Features

- Xem

## License

MIT License
