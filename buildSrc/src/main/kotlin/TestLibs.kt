/**
 * TestLibs constants.
 */
object TestLibs {
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}" // kotlin idiomatic testing
    const val mockk = "io.mockk:mockk:${Versions.mockk}" // mock framework

    const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}" // junit testing framework
    const val junitParams = "org.junit.jupiter:junit-jupiter-params:${Versions.junit}" // generated parameters for tests

    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"

    const val springTest = "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}"
    const val springSecurityTest = "org.springframework.security:spring-security-test:${Versions.spring}"

    const val springMockK = "com.ninja-squad:springmockk:${Versions.springMockK}"
}
