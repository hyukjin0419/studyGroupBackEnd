# <img src="https://github.com/user-attachments/assets/2d9d7414-a8f1-4437-bb01-14a1d6df1089" width="50" height="50" /> Sync Mate – Backend Server

[![App Store](https://img.shields.io/badge/App_Store-Download-0D96F6?style=for-the-badge&logo=apple&logoColor=white)](https://apps.apple.com/kr/app/sync-mate/id6755131308)

[🇺🇸 English](./README.md) | [🇰🇷 한국어](./README.ko.md)

🔹 [Sync Mate – Frontend (Flutter)](https://github.com/hyukjin0419/studyGroupFrontEnd)

**This is the backend API server for _Sync Mate_, a productivity tool designed to help teams and individuals achieve their goals.**  
The server is optimized for efficient communication with the Flutter mobile client, with a strong focus on data integrity, scalability, and stability.

---

## 💡 Key Technical Decisions & Problem Solving

The backend design goes beyond simple feature implementation. It emphasizes **scalability**, **security**, and **client-centered usability** for long-term maintainability.


### 1. Client-Centric API Architecture

- **View-Optimized DTOs:**  
  Instead of exposing entities directly, endpoints return **custom DTOs tailored to the needs of each mobile screen**, reducing payload size and preventing circular references.

- **Prefetch-Friendly APIs:**  
  Mobile network costs are reduced through grouped responses that minimize **round-trip API calls**, improving responsiveness and user experience.

### 2. Robust Data Integrity & Query Strategy

- **Hybrid Query Approach (JPQL + Native SQL):**  
  JPA/JPQL handles most logic, while Native Queries are used when domain constraints or performance require higher control and precision.

- **Soft Deletion:**  
  Logical deletion with a `deleted_at` timestamp prevents accidental data loss and allows recovery, while a scheduled cleanup maintains DB hygiene.

### 3. Security & Defensive Design

- **ID Obfuscation:**  
  Auto-increment primary keys are kept internally for performance, but external exposure is protected using **UUIDs and join codes**, blocking **ID enumeration attacks**.

- **Custom JWT Token Management:**  
  A complete Access/Refresh token mechanism ensures secure authentication while providing seamless UX without forced logout.


### 4. Maintainability & Code Clarity

- **Builder Pattern Across Entities:**  
  Avoids constructor overloading and keeps entity creation readable and maintainable.

- **Clear Responsibility Separation:**  
  Entity creation logic is separated from service operations, ensuring better modularity and testability.

---

## 🗂️ ERD (Entity Relationship Diagram)

<img width="990" height="969" alt="erd" src="https://github.com/user-attachments/assets/836ac9c7-fe41-426d-9465-7e55ba520a2a" />

- **User ↔ Study:**  
  A many-to-many relationship managed via the `StudyMember` join table, which also stores role (leader/member), invitation status, and historical state.

---

## 🏗️ System Architecture

<img width="1233" height="791" alt="deploy-flow" src="https://github.com/user-attachments/assets/b00391a6-06fd-42a4-b150-7359c5379d4d" />

### **Deployment Flow**

- **Mobile (Client):**  
  The Flutter app is built via Xcode and distributed through the **App Store**.

- **Backend (Server):**  
  Deployment is currently manual, uploading `.jar` build files to AWS Lightsail via FileZilla.

- **Database:**  
  Spring Boot communicates with MySQL for persistent data storage.

> **🚀 Upcoming Improvement — CI/CD**  
> Deployment will transition to a **Docker + GitHub Actions CI/CD pipeline** to enable automated, zero-downtime deployment and improve workflow efficiency.

---

## 🛠 Tech Stack

| Category | Stack |
|---------|-------|
| **Language** | ![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) |
| **Framework & Security** | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?style=for-the-badge&logo=hibernate&logoColor=white) |
| **Database** | ![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white) |
| **Infrastructure & DevOps** | ![AWS](https://img.shields.io/badge/AWS-Lightsail-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white) ![Nginx](https://img.shields.io/badge/Nginx-Server-009639?style=for-the-badge&logo=nginx&logoColor=white) ![Linux](https://img.shields.io/badge/Linux-OS-FCC624?style=for-the-badge&logo=linux&logoColor=black) |
| **Authentication** | ![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) |

---
