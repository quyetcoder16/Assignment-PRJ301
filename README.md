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
├── public               # Chứa các tệp tĩnh như favicon, hình ảnh, v.v.
└── src                  # Chứa mã nguồn chính của ứng dụng React.
    ├── App.jsx          # Component gốc của ứng dụng.
    ├── main.jsx         # Điểm vào chính của ứng dụng React.
    ├── assets           # Chứa tài nguyên như hình ảnh.
    ├── components       # Các component UI được tái sử dụng.
    ├── pages            # Các trang chính của ứng dụng.
    ├── redux            # Quản lý state bằng Redux.
    ├── routes           # Cấu hình router của ứng dụng.
    ├── service          # Gọi API và xử lý dữ liệu từ backend.
    ├── templates        # Chứa các template dùng chung.
    └── utils            # Chứa các hàm tiện ích chung.
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

1. Cài đặt Node.js (22.11.0)
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

- Xem đơn của mình
- Tạo đơn xin nghỉ
- Xét duyệt đơn
- Xem agenda

## License

MIT License
