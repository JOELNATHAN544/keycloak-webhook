plugins {
    kotlin("jvm")
}

group = "com.vymalo.keycloak.webhook"
version = "0.10.0-rc.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.apache.commons", "commons-lang3", "3.17.0")
    implementation("com.google.code.gson", "gson", "2.12.1")

    compileOnly("org.keycloak", "keycloak-services", "26.4.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

