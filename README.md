# Student Management Application – Grails (using Java + Groovy) + JWT Authentication

A secure and role-based Student Management System built with **Grails (Using Java)** backend and **JWT (JSON Web Token)** authentication. Supports role-based access for **Admin**, **Teacher**, and **Student** users. Frontend is integrated via **Stencil.js** and login uses secure cookies.

---

## Features

-  JWT-based authentication stored in HTTP-only cookies
-  Role-based access control (`ROLE_ADMIN`, `ROLE_TEACHER`, `ROLE_STUDENT`)
-  Domain model includes User, Role, Student, Teacher, Attendance, and more
-  Custom Spring Security filter for JWT validation
-  RESTful APIs with Grails Controller structure
-  Frontend integrated with Stencil.js (custom web components)

---

## Tech Stack

- **Backend:** Grails 6.2.3 (Groovy), Spring Security
- **Authentication:** JWT (via jjwt library), Cookies
- **Frontend:** Stencil.js (Web Components)
- **Database:** H2 / PostgreSQL / MySQL (Configurable)
- **Build Tool:** Gradle

---


