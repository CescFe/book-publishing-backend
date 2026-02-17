plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.diffplug.spotless")
    id("com.google.cloud.tools.jib")
    id("com.google.cloud.artifactregistry.gradle-plugin")
}

val bookPublishingApiSpec = "1.4.0"
val ktLint = "1.7.1"
val postgresql = "42.7.8"
val liquibase = "4.33.0"
val mockitoKotlin = "6.1.0"
val jjwtSecurity = "0.13.0"

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
    implementation(project(":ms-catalog"))

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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")

    runtimeOnly("org.postgresql:postgresql:$postgresql")

    implementation("org.liquibase:liquibase-core:$liquibase")

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
