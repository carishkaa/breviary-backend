import java.util.Properties

group = "blue.mild.breviary.backend"

plugins {
    application
    id("org.springframework.boot") version Versions.springBoot
    kotlin("plugin.spring") version Versions.kotlin
    kotlin("plugin.noarg") version Versions.kotlin // JPA default constructors plugin
    kotlin("plugin.jpa") version Versions.kotlin
    id("com.adarshr.test-logger") version "1.7.0"
    id("net.nemerosa.versioning") version Versions.nemerosaVersioning
}

val mClass = "blue.mild.breviary.backend.BackendApplicationKt"
val gitVersion = versioning.info?.tag ?: versioning.info?.lastTag ?: versioning.info?.commit ?: "development"
val codeVersion = gitVersion + (if (versioning.info?.isDirty == true) "-dirty" else "")
version = codeVersion

application {
    mainClass.set(mClass)
}

repositories {
    jcenter()
}

dependencies {
    implementation(Libs.jackson)

    implementation(Libs.springBootWeb) {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }

    implementation(Libs.springBootValidation)
    implementation(Libs.springBootLog4j2)
    implementation(Libs.springBootSecurity)
    implementation(Libs.springBootActuator)
    implementation(Libs.springDataJpa)

    implementation(Libs.springfoxSwagger)
    implementation(Libs.springfoxSwaggerUi)

    implementation(Libs.flyway)
    implementation(Libs.hibernateTypes)
    implementation(Libs.jacksonDataformat)
    implementation(Libs.slf4j)
    implementation(Libs.jsonwebtoken)
    implementation(Libs.kotlinLogging)
    implementation(Libs.okHttp3)

    runtimeOnly(Libs.postgresDriver)

    testImplementation(TestLibs.springTest)
    testImplementation(TestLibs.springSecurityTest)
    testImplementation(TestLibs.springMockK)

    testRuntimeOnly(Libs.postgresDriver)
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
    exclude("ch.qos.logback", "logback-classic")

    resolutionStrategy {
        force(Libs.slf4j)
    }
}

tasks {
    register<Test>("generateSwagger") {
        description = "Executes one Junit test which generates swagger."

        filter {
            includeTestsMatching("GenerateSwaggerTest")
        }
    }

    register("createVersionFile") {
        val resources = requireNotNull(sourceSets.main.get().output.resourcesDir) { "Could not access resources." }
        resources.mkdirs()
        val properties = Properties().apply {
            setProperty("codeVersion", codeVersion)
        }
        properties.store(File(resources, "version.properties").outputStream(), null)
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn("createVersionFile")
    }

    bootDistZip {
        archiveFileName.set("backend.zip")
    }
}
