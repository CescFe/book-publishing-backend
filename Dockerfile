# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Build arguments for GitHub Packages credentials
ARG GITHUB_USERNAME
ARG TOKEN_PAT

# Set environment variables for Gradle
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV TOKEN_PAT=${TOKEN_PAT}

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x gradlew

# Build the application (skip tests for faster builds)
RUN ./gradlew clean bootJar --no-daemon -x test

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=pro"]
