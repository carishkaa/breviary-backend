package blue.mild.breviary.backend.services

import blue.mild.breviary.backend.dtos.ApplicationEnvironment
import blue.mild.breviary.backend.dtos.ApplicationInfoDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import pw.forst.tools.katlib.getEnv
import java.io.File
import java.util.Properties

/**
 * Service providing information about the application version and environment.
 */
@Service
class ApplicationInfoService(
    @Value("classpath:version.properties") private val versionResource: Resource
) {

    protected companion object {
        /**
         * Default application version set in Gradle build and Docker image build.
         */
        const val DEFAULT_VERSION = "development"

        /**
         * Environment variable with path to a file with version.
         */
        const val RELEASE_FILE_ENV = "RELEASE_FILE_PATH"

        /**
         * Key under which should be the version of the code in version.properties.
         */
        const val VERSION_PROPERTIES_KEY = "codeVersion"
    }

    // cache all data as that doesn't change during the application runtime
    private val versionProperties by lazy { Properties().apply { load(versionResource.inputStream) } }
    private val version by lazy { determineApplicationVersion(versionProperties) }
    private val environment by lazy { determineApplicationEnvironment(version) }

    /**
     * Determines application version and runtime environment.
     */
    fun getApplicationInfo() = ApplicationInfoDto(
        version = version,
        environment = environment
    )

    /**
     * Determines version of the code/application.
     */
    protected fun determineApplicationVersion(versionProperties: Properties): String {
        // try to load version from the environment set in the docker image
        val versionFromEnv = runCatching {
            getEnv(RELEASE_FILE_ENV)
                ?.let { File(it).readText().trim() }
                ?.takeIf { it != DEFAULT_VERSION }
        }.getOrNull()
        // try to load version from the version.properties file set during gradle build
        val versionFromGradleBuild: String? = versionProperties.getProperty(VERSION_PROPERTIES_KEY)
        // version from env overrides version from the gradle build
        return versionFromEnv ?: versionFromGradleBuild ?: DEFAULT_VERSION
    }

    /**
     * Determines in which environment is the application running.
     */
    protected fun determineApplicationEnvironment(applicationVersion: String): ApplicationEnvironment {
        val productionRegex = "^\\d+\\.\\d+\\.\\d+\$".toRegex()
        val stagingRegex = "\\b([a-f0-9]{40})\\b".toRegex()

        return when {
            applicationVersion.matches(productionRegex) -> ApplicationEnvironment.PRODUCTION
            applicationVersion.matches(stagingRegex) -> ApplicationEnvironment.STAGING
            else -> ApplicationEnvironment.DEVELOPMENT
        }
    }
}
