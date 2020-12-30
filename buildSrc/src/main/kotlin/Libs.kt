/**
 * Libs constants.
 */
object Libs {

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}" // kotlin standard library
    const val kotlinReflection = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}" // reflection library
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}" // kotlin coroutines

    const val jackson = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}" // json serializer

    const val springBootWeb = "org.springframework.boot:spring-boot-starter-web:${Versions.springBoot}"
    const val springBootSecurity = "org.springframework.boot:spring-boot-starter-security:${Versions.springBoot}"
    const val springBootValidation = "org.springframework.boot:spring-boot-starter-validation:${Versions.springBoot}"
    const val springBootActuator = "org.springframework.boot:spring-boot-starter-actuator:${Versions.springBoot}"
    const val springBootLog4j2 = "org.springframework.boot:spring-boot-starter-log4j2:${Versions.springBoot}"
    const val springDataJpa = "org.springframework.boot:spring-boot-starter-data-jpa:${Versions.springBoot}"
    const val springfoxSwagger = "io.springfox:springfox-boot-starter:${Versions.springfox}"
    const val springfoxSwaggerUi = "io.springfox:springfox-swagger-ui:${Versions.springfox}"

    const val jaxbRuntime = "org.glassfish.jaxb:jaxb-runtime:${Versions.jaxbRuntime}"
    const val postgresDriver = "org.postgresql:postgresql:${Versions.postgresDriver}"

    const val flyway = "org.flywaydb:flyway-core:${Versions.flyway}" // flyway db migration tool

    const val hibernateTypes = "com.vladmihalcea:hibernate-types-52:${Versions.hibernateTypes}"

    const val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"

    const val jacksonDataformat = "com.fasterxml.jackson.dataformat:jackson-dataformat-csv:${Versions.jacksonDataformatVersion}"

    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"

    const val katlib = "pw.forst.tools:katlib:${Versions.katlib}"

    const val jsonwebtoken = "io.jsonwebtoken:jjwt:${Versions.jsonwebtoken}"

    const val kotlinLogging = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}" // logging DSL

    const val okHttp3 = "com.squareup.okhttp3:okhttp:${Versions.okHttp3}"
}
