import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
    id("org.springframework.boot")
}

group = "com.javabom"
version = "0.0.1"

dependencies {
    api("io.github.resilience4j:resilience4j-all:1.7.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
