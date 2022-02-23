import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    //kotlin
    base
    java // 기본적인 java task 를 제공한다. compileJava, test, jar 등..
    kotlin("jvm") version "1.4.21" apply false // apply false 로 서브 프로젝트에 일괄적용을 하지 않게 한다.

    //spring
    id("io.spring.dependency-management") version Dependencies.Versions.springDependencyManagement
    id("org.springframework.boot") version Dependencies.Versions.springBoot apply false
    kotlin("plugin.spring") version Dependencies.Versions.kotlin apply false
}

allprojects {
    group = "bom.javabom.bomkotlin"
}

val kotlinProject = arrayListOf(
    project(":racing-car"),
    project(":kotlin-in-action"),
    project(":effective-kotlin")
)

configure(kotlinProject) {
    apply {
        plugin<KotlinPlatformJvmPlugin>() // kotlin("jvm") 을 적용한다.
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        testImplementation("org.junit.jupiter", "junit-jupiter", "5.6.2")
        testImplementation("org.assertj", "assertj-core", "3.18.1")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    tasks.named<Test>("test"){
        useJUnitPlatform()
    }
}

val springProjects = arrayListOf(
    project(":bom-feign"),
    project(":bom-circuit-breaker")
)

configure(springProjects){
    apply {
        plugin<JavaLibraryPlugin>()
        plugin<KotlinPlatformJvmPlugin>()
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
    }

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("org.jetbrains.kotlin:kotlin-bom:${Dependencies.Versions.kotlin}")
            mavenBom("org.springframework.boot:spring-boot-dependencies:${Dependencies.Versions.springBoot}")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Dependencies.Versions.springCloud}")
        }
        dependencies{
            dependencySet("io.github.microutils:${Dependencies.Versions.kotlinLogging}") {
                entry("kotlin-logging-jvm")
                entry("kotlin-logging-common")
            }
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("io.github.microutils:kotlin-logging-jvm")
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<KotlinCompile> {
        sourceCompatibility = "11"

        kotlinOptions {
            freeCompilerArgs.plus("-Xjsr305=strict")
            freeCompilerArgs.plus("-Xjvm-default=enable")
            freeCompilerArgs.plus("-progressive")
            freeCompilerArgs.plus("-XXLanguage:+InlineClasses")

            jvmTarget = "11"
        }

        dependsOn("processResources")
    }

    tasks.named<Test>("test"){
        useJUnitPlatform()
    }
}
