
## 독립된 개발 환경 실행 방법

#### 1. Env 파일

```text
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/testdb?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
SPRING_DATASOURCE_USER=test
SPRING_DATASOURCE_PASSWORD=1234
SPRING_JPA_HIBERNATE_DDL_AUTO=create
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
SPRING_PROFILES_ACTIVE=dev
```

#### 2. docker 명령어

```docker
docker compose -f ./docker-compose.dev.yml up -d
```

실행