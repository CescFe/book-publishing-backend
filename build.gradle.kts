plugins {
    kotlin("jvm") version "2.1.21" apply false
    kotlin("plugin.spring") version "2.1.21" apply false
    kotlin("plugin.jpa") version "2.1.21" apply false
    id("org.springframework.boot") version "3.5.9" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.diffplug.spotless") version "8.0.0" apply false
    id("com.google.cloud.tools.jib") version "3.4.5" apply false
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.2.0" apply false
}

group = "org.cescfe"
version = "0.5.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}
