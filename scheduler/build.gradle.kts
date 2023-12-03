import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("kotlin-app.base")
    id("org.graalvm.buildtools.native")
}

val koraBom: Configuration by configurations.creating
configurations {
    ksp.extendsFrom(named("koraBom"))
    implementation.extendsFrom(named("koraBom"))
}

val koraVersion: String by properties
dependencies {
    koraBom(platform("ru.tinkoff.kora:kora-parent:$koraVersion"))

    ksp("ru.tinkoff.kora:symbol-processors")

    ksp("ru.tinkoff.kora:json-annotation-processor")
    implementation("ru.tinkoff.kora:json-module")
    implementation("ru.tinkoff.kora:http-server-undertow")
    implementation("ru.tinkoff.kora:config-hocon")
    implementation("ru.tinkoff.kora:micrometer-module")
    implementation("ru.tinkoff.kora:json-module")
    implementation("ru.tinkoff.kora:logging-logback")
    implementation("ru.tinkoff.kora:http-client-jdk")
    implementation("ru.tinkoff.kora:database-jdbc")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("org.liquibase:liquibase-core:4.25.0")
    implementation("org.postgresql:postgresql:42.7.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("ru.tinkoff.kora:test-junit5")

    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation("com.jayway.jsonpath:json-path:2.8.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

graalvmNative {
    binaries {
        named("main") {
            javaLauncher = javaToolchains.launcherFor {
                languageVersion = JavaLanguageVersion.of(17)
                vendor = JvmVendorSpec.matching("GraalVM Community")
            }
            imageName = "application"
            mainClass = "online.ilyin.SchedulerApplication"
            debug = true
            verbose = true
            buildArgs.add("--report-unsupported-elements-at-runtime")
        }
    }
    metadataRepository {
        enabled = true
    }
}