import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("net.troja.eve:eve-esi:4.1.0")

    implementation("io.javalin:javalin:3.13.3")

    implementation("org.optaplanner:optaplanner-core:8.2.0.Final")
    implementation("io.github.oliviercailloux:google-or-tools:6.7.2")

}
