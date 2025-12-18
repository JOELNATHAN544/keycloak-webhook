
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

    implementation(project(":keycloak-webhook-provider-core"))

    compileOnly("org.keycloak", "keycloak-services", "26.4.0")
    
    compileOnly("com.google.code.gson", "gson", "2.12.1")
    compileOnly("com.rabbitmq", "amqp-client", "5.21.0")
    compileOnly("org.slf4j", "slf4j-log4j12", "2.0.17")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
