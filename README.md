# 📚 StudyFlow: Smart Academic Planner

**StudyFlow** is a **Spring Boot** backend that helps students plan, track, and manage their study activities efficiently. Users can create subjects, manage tasks, and collaborate in real-time focus rooms with Pomodoro timers. The backend ensures secure authentication using JWT and isolates user-specific data.

---

## 📌 Features Implemented ✅
- **🔐 JWT Authentication**
  - Register and login with secure JWT tokens.
  - Ensures each user accesses only their own data.
- **📚 Subject Management**
  - Create, read, update, and delete subjects.
  - Subjects are scoped per authenticated user.
- **📝 Task Management**
  - CRUD operations for tasks under each subject.
  - Tasks include title, description, and due dates.
- **⏱ Real-Time Focus Rooms**
  - Join collaborative study sessions with Pomodoro timers.
  - Real-time updates using **WebSocket + STOMP**.

---

## 🚀 Tech Stack
| Layer | Technology |
|-------|------------|
| Backend | Spring Boot (Java) |
| Database | MySQL (Dockerized recommended) |
| Authentication | JWT + Spring Security |
| Real-Time | WebSocket + STOMP |
| Build | Maven |
| Testing | JUnit |



## 🛠️ Getting Started

### **Prerequisites**
- Java 17+
- Maven
- MySQL
- Docker (if using Dockerized MySQL)


### **Setup**
1. Clone the repository:
```bash
git clone https://github.com/Chaitanya3107/StudySync.git
cd StudySync
```
2. Build the project with Maven:
```bash
./mvnw clean install
```
4. Configure application.yml with your database credentials.

5. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
6. Access API endpoints via http://localhost:8080/.

---

## 🧩 API Flow Diagram

```mermaid
 graph TD
    A[User] -->|Login / Register| B[Auth Controller]
    B -->|Validate Credentials| C[Auth Service]
    C -->|Generate Token| D[JWT Token]
    D -->|Authorize Requests| E[Spring Security Filter]

    E -->|Create Subject| F[Subject Controller]
    F -->|Call Business Logic| G[Subject Service]
    G -->|Store / Fetch Subjects| H[(Subject Repository)]

    E -->|Create Task| I[Task Controller]
    I -->|Call Business Logic| J[Task Service]
    J -->|Store / Fetch Tasks| K[(Task Repository)]

    E -->|Join Focus Room| L[FocusRoom Controller]
    L -->|Handle Real-Time Events| M[FocusRoom Service]
    M -->|Broadcast Updates| N[(Active WebSocket Sessions)]

    H -->|Read/Write Data| O[(MySQL Database)]
    K -->|Read/Write Data| O
    M -->|Optional persistence| O

    A -->|Owns| F
    F -->|Has Many| I
    I -->|Belongs To| F

    subgraph Authentication Layer
        B
        C
        D
        E
    end

    subgraph Core Application
        F
        G
        H
        I
        J
        K
    end

    subgraph Real-Time Layer
        L
        M
        N
    end

    subgraph Data Layer
        O
    end

    classDef core fill:#2563eb,stroke:#1e3a8a,color:#fff,stroke-width:1px;
    classDef data fill:#047857,stroke:#064e3b,color:#fff,stroke-width:1px;
    class A,B,C,D,E,F,G,H,I,J,K,L,M,N core;
    class O data;

```

## 📦 Future Work
  - Scheduled Email Reminders - Send automatic email notifications for incomplete tasks.
  - AI Study Assistant - Integrate Spring AI to summarize notes and answer study questions.
  - AWS Deployment - Deploy backend on AWS EC2 with S3 for storage of uploaded files.



  
