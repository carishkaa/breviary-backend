import java.util.Properties

group = "blue.mild.breviary.backend"

plugins {
    application
    id("org.springframework.boot") version Versions.springBoot
    kotlin("plugin.spring") version Versions.kotlin
    id("org.jetbrains.kotlin.plugin.noarg") version Versions.kotlin // JPA default constructors plugin
    id("org.jetbrains.kotlin.plugin.jpa") version Versions.kotlin
    id("com.adarshr.test-logger") version "1.7.0"
    id("net.nemerosa.versioning") version Versions.nemerosaVersioning
}

val mainClass = "blue.mild.breviary.backend.BackendApplicationKt"

application {
    mainClassName = mainClass.toString()
}

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    implementation(Libs.kotlinStdlib)
    implementation(Libs.kotlinReflection)
    implementation(Libs.kotlinCoroutines)
    implementation(Libs.jackson)

    implementation(Libs.springBootWeb) {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
    implementation(Libs.springBootValidation)
    implementation(Libs.springBootLog4j2)
    implementation(Libs.springBootSecurity)
    implementation(Libs.springBootActuator)
    implementation(Libs.springDataJpa)
    implementation(Libs.springThymeleaf)

    implementation(Libs.springfoxSwagger)
    implementation(Libs.springfoxSwaggerUi)

    implementation(Libs.javaxXmlJaxb)

    implementation(Libs.flyway)

    implementation(Libs.hibernateTypes)

    implementation(Libs.jacksonDataformat)

    implementation(Libs.gson)

    implementation(Libs.slf4j)

    implementation(Libs.ktoolz)

    runtimeOnly(Libs.jaxbRuntime)
    runtimeOnly(Libs.postgresDriver)

    testImplementation(TestLibs.springTest)
    testImplementation(TestLibs.springSecurityTest)

    testImplementation(TestLibs.springMockK)

    testRuntimeOnly(Libs.postgresDriver)

    implementation(Libs.jsonwebtoken)

    implementation(Libs.kotlinLogging)

    implementation(Libs.okHttp3)

    // testing
    testImplementation(TestLibs.kotlinTest) // kotlin idiomatic testing
    testImplementation(TestLibs.mockk) // mock framework

    testImplementation(TestLibs.junitApi) // junit testing framework
    testImplementation(TestLibs.junitParams) // generated parameters for tests

    testRuntime(TestLibs.junitEngine) // testing runtime
    implementation(kotlin("stdlib"))
}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
        exclude("ch.qos.logback", "logback-classic")

        resolutionStrategy {
            force(Libs.slf4j)
        }
    }
}

tasks {
    register<Test>("generateSwagger") {
        description = "Executes one Junit test which generates swagger."

        filter {
            includeTestsMatching("GenerateSwaggerTest")
        }
    }
}

val version: String = versioning.info?.tag ?: versioning.info?.lastTag ?: "development"

task("createVersionFile") {
    val resources = requireNotNull(sourceSets.main.get().output.resourcesDir) { "Could not access resources." }
    resources.mkdirs()
    val properties = Properties().apply {
        setProperty("app", version)
    }
    properties.store(File(resources, "version.properties").outputStream(), null)
}
