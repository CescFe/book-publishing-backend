FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY app app
COPY ms-catalog ms-catalog
COPY api-catalog api-catalog

RUN chmod +x gradlew
RUN ./gradlew :app:clean :app:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=pro"]
