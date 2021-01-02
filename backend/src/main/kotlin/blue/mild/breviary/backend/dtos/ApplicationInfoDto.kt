package blue.mild.breviary.backend.dtos

/**
 * VersionInfoDtoOut.
 *
 * @property version
 */
data class ApplicationInfoDto(
    val version: String,
    val environment: ApplicationEnvironment
)

/**
 * Runtime application environment.
 */
enum class ApplicationEnvironment {
    PRODUCTION, STAGING, DEVELOPMENT
}

