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