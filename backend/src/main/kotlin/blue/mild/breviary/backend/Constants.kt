package blue.mild.breviary.backend

import blue.mild.breviary.backend.services.toLocalDateTimeInUTC
import java.time.Instant

/**
 * Project constants.
 */
object Project {
    val STRING_CHARSET = Charsets.UTF_8
    val YEAR = Instant.now().toLocalDateTimeInUTC().year
    const val EMPTY_STRING = ""
}

/**
 * Common application configuration constants.
 */
object App {
    const val JPA_REPOSITORIES_SCAN_BASE_PACKAGES = "blue.mild.breviary.backend.db.repositories.**"
    const val ENTITY_SCAN_BASE_PACKAGES = "blue.mild.breviary.backend.db.entities.**"
    const val SPRING_BOOT_APP_BASE_PACKAGES = "blue.mild.**"
    const val DB_PREFIX = "spring.datasource"
}

/**
 * API routes.
 */
object ApiRoutes {
    const val BASE_PATH = "\${spring.data.rest.base-path}"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val LOGOUT = "logout"
    const val VERSION = "version"
    const val USER = "user"
    const val ERROR = "error"
    const val HEPARIN_PATIENTS = "heparin-patients"
    const val INSULIN_PATIENTS = "insulin-patients"
    const val HEPARIN_PATIENT_HISTORY_ENTRIES = "$HEPARIN_PATIENTS/history-entries"
    const val INSULIN_PATIENTS_HISTORY_ENTRIES = "$INSULIN_PATIENTS/history-entries"
    const val HEPARIN_RECOMMENDATION = "heparin-recommendation"
    const val INSULIN_RECOMMENDATION = "insulin-recommendation"

    /**
     * Password reset routes.
     */
    object PasswordReset {
        const val BASE = "password-reset"
        const val REQUEST = "request"
    }

    /**
     * Password change routes.
     */
    object PasswordChange {
        const val BASE = "password-change"
    }
}

/**
 * Some of timezones defined in https://data.iana.org/time-zones/releases/.
 */
object TimeZones {
    const val PRAGUE = "Europe/Prague"
    const val UTC = "Etc/UTC"
}

/**
 * Error parameter constants.
 */
object PropertiesNames {
    const val ID = "id"
    const val NAME = "name"
    const val NAMES = "names"
    const val EMAIL = "email"
    const val ACTIVE_USER = "activeUser"
    const val PASSWORD = "password"
    const val NEW_PASSWORD = "newPassword"
    const val START_TIME = "startTime"
    const val END_TIME = "endTime"
    const val VALID_UNTIL = "validUntil"
    const val VALUE = "value"
}

const val EMPTY_USERNAME = Project.EMPTY_STRING
const val EMPTY_PASSWORD = Project.EMPTY_STRING
