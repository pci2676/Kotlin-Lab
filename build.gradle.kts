import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    base
    java // 기본적인 java task 를 제공한다. compileJava, test, jar 등..
    kotlin("jvm") version "1.4.21" apply false // apply false 로 서브 프로젝트에 일괄적용을 하지 않게 한다.
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1" apply false// kotlin 공식 코딩 컨벤션을 맞춰준다. https://github.com/JLLeitschuh/ktlint-gradle
}

allprojects {
    group = "bom.javabom.bomkotlin"
}

val kotlinProject = arrayListOf(
    project(":racing-car")
)

configure(kotlinProject) {
    apply {
        plugin<KotlinPlatformJvmPlugin>() // kotlin("jvm") 을 적용한다.
        plugin("org.jlleitschuh.gradle.ktlint") // ktlint 적용한다.
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

    configure<KtlintExtension> {
        verbose.set(true)

        disabledRules.addAll(
            "import-ordering",
            "comment-spacing",
            "no-blank-line-before-rbrace"
        )
    }

    tasks.withType<Test>{
        useJUnitPlatform()
    }
}
