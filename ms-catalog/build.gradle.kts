plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.artifactregistry.gradle-plugin")
}

val bookPublishingApiSpec = "1.4.0"
val postgresql = "42.7.8"
val liquibase = "4.33.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven {
        name = "ArtifactRegistry"
        url = uri("artifactregistry://europe-west1-maven.pkg.dev/book-publishing-backend/maven-repo")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.cescfe:book-publishing-api-spec:$bookPublishingApiSpec")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.37")
    implementation("io.swagger.core.v3:swagger-models:2.2.37")
    implementation("io.swagger.core.v3:swagger-core:2.2.37")

    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")
    implementation("org.liquibase:liquibase-core:$liquibase")
    runtimeOnly("org.postgresql:postgresql:$postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.1.0")
    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.security:spring-security-oauth2-resource-server")
    testImplementation("org.springframework.security:spring-security-oauth2-jose")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
