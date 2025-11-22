plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.spring") version "2.1.21"
    kotlin("plugin.jpa") version "2.1.21"
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "8.0.0"
    id("com.google.cloud.tools.jib") version "3.4.5"
}

group = "org.cescfe"
version = "0.2.0"

val bookPublishingApiSpec = "1.0.0"
val ktLint = "1.7.1"
val postgresql = "42.7.8"
val liquibase = "4.33.0"
val mockitoKotlin = "6.1.0"
val jjwtSecurity = "0.13.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/CescFe/book-publishing-api-spec")
        credentials {
            username = System.getenv("GITHUB_USERNAME") ?: project.findProperty("github.username") as String?
            password = System.getenv("TOKEN_PAT") ?: project.findProperty("github.token") as String?
        }
    }
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // API Specification Library
    implementation("org.cescfe:book-publishing-api-spec:$bookPublishingApiSpec")

    // Swagger/OpenAPI dependencies
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.37")
    implementation("io.swagger.core.v3:swagger-models:2.2.37")
    implementation("io.swagger.core.v3:swagger-core:2.2.37")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    // Persistence
    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")

    // Database drivers
    runtimeOnly("org.postgresql:postgresql:$postgresql")

    // Database migration
    implementation("org.liquibase:liquibase-core:$liquibase")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlin")
    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.security:spring-security-oauth2-resource-server")
    testImplementation("org.springframework.security:spring-security-oauth2-jose")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:$jjwtSecurity")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtSecurity")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtSecurity")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

listOf(tasks.compileJava, tasks.compileKotlin, tasks.compileTestJava, tasks.compileTestKotlin).forEach {
    it.get().mustRunAfter(tasks.spotlessCheck)
}

spotless {
    kotlin {
        ktlint(ktLint)
            .setEditorConfigPath("$rootDir/.editorconfig")
        targetExclude(
            "**/build/**/*.kt",
            "**/generated/**/*.kt",
            "**/build/generated-sources/**/*.kt",
            "**/build/generated-test-sources/**/*.kt",
        )
    }
    kotlinGradle {
        ktlint(ktLint)
            .setEditorConfigPath("$rootDir/.editorconfig")
        target("*.gradle.kts")
    }

    format("misc") {
        target("*.md", ".gitignore")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

jib {
    from.image = "amazoncorretto:21.0.4-al2023-headless"
    container {
        args =
            listOf(
                "--add-opens",
                "java.base/java.net=ALL-UNNAMED",
                "--add-opens",
                "java.base/sun.net.www.protocol.https=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.util=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.lang=ALL-UNNAMED",
            )
        ports = listOf("8080")
        workingDirectory = "/book-publishing-backend"
    }
}
