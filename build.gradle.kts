import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("java")
    id("kotlin-app.base") apply false
    id("com.google.devtools.ksp") apply false
    id("org.graalvm.buildtools.native") version "0.9.28" apply false
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    kotlin("plugin.spring") version "1.9.20" apply false
}

allprojects {
    group = "online.ilyin"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}