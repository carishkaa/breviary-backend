package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.dtos.ApplicationEnvironment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.core.io.Resource
import pw.forst.tools.katlib.getEnv
import java.io.File
import java.util.Properties
import java.util.UUID

class ApplicationInfoServiceTest {
    private class TestService(versionResource: Resource) : ApplicationInfoService(versionResource) {
        val defaultVersion = DEFAULT_VERSION
        val releaseEnv = RELEASE_FILE_ENV
        val propKey = VERSION_PROPERTIES_KEY
        fun appEnvFromVersion(version: String) = determineApplicationEnvironment(version)
        fun version(properties: Properties) = determineApplicationVersion(properties)
    }

    @Test
    fun `test determineApplicationEnvironment should return production`() {
        val instance = TestService(mockk())
        assertEquals(ApplicationEnvironment.PRODUCTION, instance.appEnvFromVersion("1.1.1"))
        assertEquals(ApplicationEnvironment.PRODUCTION, instance.appEnvFromVersion("29.39.1000"))
    }

    @Test
    fun `test determineApplicationEnvironment should return staging`() {
        val instance = TestService(mockk())
        assertEquals(ApplicationEnvironment.STAGING, instance.appEnvFromVersion("540f8a7bcbb7116e6aff3d633aba2f2ab8af4096"))
        assertEquals(ApplicationEnvironment.STAGING, instance.appEnvFromVersion("d48152edc75c32aa799af14d7fc370ff6bb2d478"))
    }

    @Test
    fun `test determineApplicationEnvironment should return development`() {
        val instance = TestService(mockk())
        assertEquals(ApplicationEnvironment.DEVELOPMENT, instance.appEnvFromVersion(instance.defaultVersion))
        assertEquals(ApplicationEnvironment.DEVELOPMENT, instance.appEnvFromVersion("1.1.1-dirty"))
        assertEquals(ApplicationEnvironment.DEVELOPMENT, instance.appEnvFromVersion("12345"))
        assertEquals(ApplicationEnvironment.DEVELOPMENT, instance.appEnvFromVersion("1.1"))
    }

    @Test
    fun `test should determine correct version from env as default is in properties`() {
        val instance = TestService(mockk())

        val expectedVersion = UUID.randomUUID().toString()
        assertNotEquals(instance.defaultVersion, expectedVersion)
        // env has correct non-default version
        val versionFile = File
            .createTempFile("env-test-${UUID.randomUUID()}", ".txt")
            .apply { writeText(expectedVersion) }
        // properties have the default version
        val props = Properties().apply { setProperty(instance.propKey, instance.defaultVersion) }

        mockkStatic("pw.forst.tools.katlib.OtherExtensionsKt")
        every { getEnv(instance.releaseEnv) } returns versionFile.absolutePath

        assertEquals(expectedVersion, instance.version(props))
    }

    @Test
    fun `test should determine correct version from properties as env returns default`() {
        val instance = TestService(mockk())

        val expectedVersion = UUID.randomUUID().toString()
        assertNotEquals(instance.defaultVersion, expectedVersion)
        // env has correct non-default version
        val versionFile = File
            .createTempFile("env-test-${UUID.randomUUID()}", ".txt")
            .apply { writeText(instance.defaultVersion) }
        // properties have the default version
        val props = Properties().apply { setProperty(instance.propKey, expectedVersion) }

        mockkStatic("pw.forst.tools.katlib.OtherExtensionsKt")
        every { getEnv(instance.releaseEnv) } returns versionFile.absolutePath

        assertEquals(expectedVersion, instance.version(props))
    }

    @Test
    fun `test should determine correct version from properties as env doesnt exist`() {
        val instance = TestService(mockk())
        val expectedVersion = instance.defaultVersion
        // properties have the default version
        val props = Properties().apply { setProperty(instance.propKey, expectedVersion) }
        assertEquals(expectedVersion, instance.version(props))
    }
}
