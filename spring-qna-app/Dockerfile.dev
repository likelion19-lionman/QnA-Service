FROM gradle:8.14.3-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle gradle

#의존성 다운로드 (캐시 레이어)
RUN gradle dependencies --no-daemon --quiet || return 0
COPY src ./src

RUN gradle bootJar -x test --no-daemon --parallel

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-Xms128m", \
    "-Xmx256m", \
    "-XX:+UseG1GC", \
    "-XX:+TieredCompilation", \
    "-XX:TieredStopAtLevel=1", \
    "-jar", "app.jar"]