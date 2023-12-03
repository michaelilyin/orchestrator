plugins {
    id("java")
    id("java-library")
}

java {
    toolchain {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


tasks.test {
    useJUnitPlatform()
}