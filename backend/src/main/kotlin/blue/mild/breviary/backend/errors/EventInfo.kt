package blue.mild.breviary.backend.errors

/**
 * EventInfo.
 *
 * @property code
 * @property action
 * @property outcome
 * @property stackTrace
 * @property startTimestamp
 * @property duration
 * @property applicationName
 */
data class EventInfo(
    val code: Int,
    val action: String,
    val outcome: String,
    val stackTrace: String?,
    val startTimestamp: String?,
    val duration: Long = 0,
    val applicationName: String = "TASP-COVID"
)
