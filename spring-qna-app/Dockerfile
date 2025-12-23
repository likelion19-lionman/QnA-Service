# 1단계: Build Stage
FROM gradle:8-jdk17 AS builder
WORKDIR /app
# 소스 코드 복사
COPY . .
# 테스트 제외하고 빌드
RUN gradle clean build -x test
# 2단계: Run Stage
FROM eclipse-temurin:17-jre
WORKDIR /app
# 빌드된 jar만 복사
COPY --from=builder /app/build/libs/*.jar app.jar
# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
