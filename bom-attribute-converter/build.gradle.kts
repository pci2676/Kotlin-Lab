import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
    mainClassName = "com.javabom.bomkotlin.main.AttributeConverterApplicationKt"
    archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
    id("org.springframework.boot")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter")
    runtimeOnly("com.h2database:h2")
}
