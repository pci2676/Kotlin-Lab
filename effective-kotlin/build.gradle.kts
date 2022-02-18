repositories {
    jcenter()
}
plugins {
    `kotlin-dsl`
}
dependencies {
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}
