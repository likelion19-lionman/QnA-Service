# QnA-Service

멋쟁이 사자처럼 19기 1차 프로젝트

### 개발 환경 구동

**전체 실행**
```docker
docker compose -f docker-compose.dev.yml up -d
```

**Front 만 실행**

```bash
cd nextjs-qna-app && docker compose -f docker-compose.dev.yml up -d
```

**Back 만 실행**

```bash
cd spring-qna-app && docker compose -f docker-compose.dev.yml up -d
```


# QnA-Service

멋쟁이 사자처럼 19기 6조 lionman-1차 프로젝트

## 프로젝트 개요 ##

이 프로젝트는 QnA서비스를 제공하는 웹 애플리케이션입니다.  
사용자는 회원가입/로그인 후 질문을 게시하고, 다른 사용자들이 답변(댓글)을 달 수 있습니다.  
관리자는 실시간으로 게시글을 관리할 수 있습니다.  

## 핵심 기능 ##

### 1. 사용자 관점 기능
- 회원가입 / 로그인 (이메일 인증 기반)
- 질문 게시 및 조회 (페이징 지원)
- 답변(댓글) 작성 및 조회
- 게시글 삭제 (본인 게시글만)

### 2. 관리자/운영자 관점 기능
- 전체 게시글 모니터링 및 관리

### 3. 기타 기능
- JWT 기반 인증 (액세스/리프레시 토큰)
- 이메일 인증 코드 전송 및 검증
- CORS 설정으로 프론트엔드와 백엔드 통신
- Docker Compose를 통한 마이크로서비스 배포

## 기술 스택 ##

Frontend: Next.js, React, Tailwind CSS  
Backend: Spring Boot (Java), JPA/Hibernate  
Database: MySQL  
Infra / DevOps: Docker, Docker Compose, Nginx  
Tools: Git (GitHub), Notion (문서관리), Miro (설계)

## 시스템 아키텍처 ##

이 프로젝트는 마이크로서비스 아키텍처를 사용함
- 프론트엔드(Next.js)는 백엔드(Spring Boot)와 REST API로 통신 
- 인증은 JWT 토큰 기반으로, 쿠키에 액세스 토큰을 저장 
- 데이터베이스는 MySQL을 사용하며, JPA로 ORM 처리  
- Nginx가 프록시 역할을 하여 요청을 분배  
- 전체 서비스는 Docker Compose로 컨테이너화되어 실행됨  

간단한 아키텍처:  
Client (Next.js) ↔ Nginx Proxy ↔ Spring Boot API ↔ MySQL DB

## 프로젝트 구조 ##
```
spring-qna-app/ # Spring Boot 백엔드
├─ src/main/java/com/example/springqnaapp/
│ ├─ controller/ # API 컨트롤러 (AuthController, QnaController)
│ ├─ service/ # 비즈니스 로직
│ ├─ repository/ # 데이터 접근
│ ├─ domain/ # 엔티티
│ ├─ config/ # 설정 클래스
│ ├─ security/ # 보안 설정
│ └─ common/ # 공통 DTO, 유틸
└─ .env.dev # 환경 변수

nextjs-qna-app/ # Next.js 프론트엔드
├─ app/
│ ├─ api/ # API 라우트
│ ├─ auth/ # 인증 페이지
│ ├─ qna/ # QnA 페이지
│ └─ layout.js # 레이아웃
├─ components/ # 재사용 컴포넌트
└─ .env.dev # 환경 변수

mysql-qna-db/ # MySQL 설정
├─ Dockerfile
└─ init/ # 초기 SQL 스크립트

nginx-qna-proxy/ # Nginx 프록시
└─ default.conf
```

## 실행 방법 ##

요구 환경: Docker, Docker Compose 설치

## 전체 실행 ##
```bash
docker compose -f docker-compose.dev.yml up -d
```

## 환경 변수 (.env.dev)##
```
백엔드 (spring-qna-app/.env.dev)
SPRING_DATASOURCE_URL: MySQL 연결 URL
SPRING_DATASOURCE_USERNAME: DB 사용자명
SPRING_DATASOURCE_PASSWORD: DB 비밀번호
SPRING_JPA_HIBERNATE_DDL_AUTO: 스키마 자동 생성
SPRING_JPA_SHOW_SQL: SQL 로그 출력
SPRING_MAIL_HOST: 이메일 호스트
SPRING_MAIL_PORT: 이메일 포트
SPRING_MAIL_USERNAME: 이메일 계정
SPRING_MAIL_PASSWORD: 이메일 비밀번호
JWT_ACCESS_SECRET_KEY: JWT 액세스 토큰 키
JWT_REFRESH_SECRET_KEY: JWT 리프레시 토큰 키
JWT_ACCESS_TOKEN_EXPIRATION: 액세스 토큰 만료 시간
JWT_REFRESH_TOKEN_EXPIRATION: 리프레시 토큰 만료 시간
MYSQL_USER: MySQL 사용자
MYSQL_PASSWORD: MySQL 비밀번호
MYSQL_DATABASE: MySQL DB명
APP_CORS_ALLOWED_ORIGINS: CORS 허용 오리진
프론트엔드 (nextjs-qna-app/.env.dev)
NODE_ENV: 환경 모드
WATCHPACK_POLLING: 파일 감시 폴링
CHOKIDAR_USEPOLLING: Chokidar 폴링
```
