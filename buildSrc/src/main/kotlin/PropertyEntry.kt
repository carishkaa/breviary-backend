import java.io.File
import java.util.Properties

/**
 * PropertyEntry.
 *
 * @property properties
 * @property propertyName
 * @property environmentName
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class PropertyEntry(
    val properties: Properties,
    val propertyName: String,
    val environmentName: String
) {
    companion object {
        private val propsFile = File(System.getenv("PROJECT_ROOT") ?: ".", "breviary.properties")
        private val envFile = File(System.getenv("PROJECT_ROOT") ?: "../", ".env")
        private val properties = Properties()
        private val envProperties = Properties()

        init {
            if (propsFile.exists()) properties.load(propsFile.bufferedReader())
            if (envFile.exists()) envProperties.load(envFile.bufferedReader())
        }

        internal fun create(propertyName: String, environmentName: String) =
            PropertyEntry(
                properties = properties,
                propertyName = propertyName,
                environmentName = environmentName
            )
    }

    /**
     * Gets env or null.
     *
     * @return
     */
    fun getEnvOrNull(): String? = System.getenv(environmentName) ?: envProperties.getProperty(environmentName)

    /**
     * Gets env or default.
     *
     * @param default
     * @return
     */
    fun getEnvOrDefault(default: String = ""): String = getEnvOrNull() ?: default

    /**
     * Gets env.
     *
     * @return
     */
    fun getEnv(): String = getEnvOrNull()
        ?: throw PropertyNotFoundException("Property '$environmentName' not found within environment variables.")

    /**
     * Gets property or null.
     *
     * @return
     */
    fun getPropOrNull(): String? = properties.getProperty(propertyName)

    /**
     * Gets property or default.
     *
     * @return
     */
    fun getPropOrDefault(default: String = ""): String = getPropOrNull() ?: default

    /**
     * Gets property.
     *
     * @return
     */
    fun getProp(): String = getPropOrNull()
        ?: throw PropertyNotFoundException("Property '$propertyName' not found in local properties file.")

    /**
     * Gets or null.
     *
     * @return
     */
    fun getOrNull(): String? = getEnvOrNull() ?: getPropOrNull()

    /**
     * Gets or default.
     *
     * @return
     */
    fun getOrDefault(default: String = ""): String = getOrNull() ?: default

    /**
     * Gets value.
     *
     * @return
     */
    fun get(): String = getOrNull()
        ?: throw PropertyNotFoundException(
            "Neither '$environmentName' was found within environment variables nor '$propertyName' was found in local properties file."
        )
}

/**
 * PropertyNotFoundException.
 *
 * @param message
 */
class PropertyNotFoundException(message: String) : Exception(message)
