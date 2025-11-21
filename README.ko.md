# <img src="https://github.com/user-attachments/assets/2d9d7414-a8f1-4437-bb01-14a1d6df1089" width="50" height="50" /> Sync Mate - Backend Server

[![App Store](https://img.shields.io/badge/App_Store-Download-0D96F6?style=for-the-badge&logo=apple&logoColor=white)](https://apps.apple.com/kr/app/sync-mate/id6755131308)

[🇺🇸 English](./README.md) | [🇰🇷 한국어](./README.ko.md)

- The service is only available in Korean for now.
---

# Sync Mate

*Collaborative study-group management app built with Spring Boot and Flutter.*

<!-- 여기 아래부터는 영어 버전 README 내용 -->


**팀 및 개인의 목표 달성을 돕는 'Sync Mate' 서비스의 실용적인 API 서버입니다.**
모바일 클라이언트(Flutter)와의 효율적인 데이터 통신과 안정적인 데이터 무결성 보장을 최우선 목표로 설계되었습니다.

## 💡 Key Technical Decisions & Problem Solving

단순 기능 구현을 넘어, **확장성(Scalability)** 과 **보안(Security)**, 그리고 **클라이언트 사용성**을 고려하여 기술을 선정하고 설계했습니다.

### 1. Client-Centric API Architecture (프론트엔드 친화적 설계)
* **View-Optimized DTO:** 엔티티를 직접 반환하지 않고, 모바일 화면별 요구사항에 맞춘 **Custom DTO**를 설계하여 데이터 오버헤드를 줄이고 순환 참조 문제를 방지했습니다.
* **Prefetch Support:** 모바일 환경의 네트워크 비용을 절감하기 위해, 클라이언트가 필요한 데이터를 한 번에 조회할 수 있도록 연관 데이터를 응집한 API를 제공하여 **API 호출 횟수(Round Trip)를 최소화**했습니다.

### 2. Robust Data Integrity & Querying (데이터 무결성 및 쿼리)
* **Hybrid Query Strategy:** 기본 로직은 JPA/JPQL을 사용하되, 복잡한 도메인 제약 조건(SQL Constraints) 검증이 필요한 로직에는 **Native Query**를 사용하여 정합성과 성능을 확보했습니다.
* **Soft Deletion:** 실수로 인한 데이터 유실을 방지하고 추후 복구를 지원하기 위해 `deleted_at` 타임스탬프를 활용한 **논리적 삭제(Soft Delete)** 를 구현했습니다.

### 3. Security & Defensive Design (보안 및 방어적 설계)
* **ID Obfuscation (ID 난독화):** DB 내부 성능을 위해 PK는 Auto Increment를 사용하되, 외부 초대 링크나 API 노출 시에는 **UUID** 및 **Joined Code**를 사용하여 **ID 열거 공격(Enumeration Attack)** 을 원천 차단했습니다.
* **Token Management:** JWT Access/Refresh Token 메커니즘을 직접 구현하여 보안성을 유지하면서도 끊김 없는 사용자 경험(UX)을 제공했습니다.

### 4. Maintainability (유지보수성)
* **Clean Builder Pattern:** 생성자 오버로딩의 복잡성을 피하기 위해 **Builder 패턴**을 전면 도입했으며, Service 계층 내에 엔티티 생성 로직이 혼재되지 않도록 책임을 분리하여 가독성을 높였습니다.






## 🗂️ ERD (Entity Relationship Diagram)
<img width="990" height="969" alt="erd" src="https://github.com/user-attachments/assets/836ac9c7-fe41-426d-9465-7e55ba520a2a" />

* **User - Study:** 다대다(N:M) 관계를 `StudyMember` 중간 테이블로 풀어서 회원의 Role(방장/팀원) 및 가입 상태를 유연하게 관리했습니다.


## 🏗️ System Architecture
<img width="1233" height="791" alt="배포 현황" src="https://github.com/user-attachments/assets/b00391a6-06fd-42a4-b150-7359c5379d4d" />

**[Deployment Flow]**
* **Mobile (Client):** Flutter로 개발된 앱은 Xcode 빌드 과정을 거쳐 **App Store**를 통해 사용자에게 배포됩니다.
* **Backend (Server):** 현재는 FileZilla를 통해 AWS 서버로 빌드 파일(Jar)을 직접 배포하는 방식을 사용 중입니다.
* **Database:** Spring Boot 서버는 MySQL 데이터베이스와 통신하여 데이터를 영구 저장합니다.

> **🚀 Upcoming Improvements (CI/CD)**
>
> 현재의 수동 배포(FileZilla) 방식에서 벗어나, **Docker**와 **GitHub Actions**를 도입하여 **CI/CD 파이프라인을 구축 중**입니다. 이를 통해 무중단 배포 환경을 마련하여 개발 효율성을 높일 예정입니다.

## 🛠 Tech Stack

| Category | Stack |
|---------|-------|
| **Language** | ![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) |
| **Framework & Security** | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?style=for-the-badge&logo=hibernate&logoColor=white) |
| **Database** | ![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white) |
| **Infrastructure & DevOps** | ![AWS](https://img.shields.io/badge/AWS-Lightsail-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white) ![Nginx](https://img.shields.io/badge/Nginx-Server-009639?style=for-the-badge&logo=nginx&logoColor=white) ![Linux](https://img.shields.io/badge/Linux-OS-FCC624?style=for-the-badge&logo=linux&logoColor=black) |
| **Authentication** | ![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) |

