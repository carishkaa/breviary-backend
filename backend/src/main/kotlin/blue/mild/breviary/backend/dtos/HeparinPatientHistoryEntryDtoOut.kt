package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * HeparinPatientHistoryEntryDtoOut.
 */
data class HeparinPatientHistoryEntryDtoOut(
    @JsonProperty(required = true)
    val date: Instant,

    @JsonProperty(required = true)
    val aptt: Float,

    @JsonProperty(required = true)
    val bolus: Float,

    @JsonProperty(required = true)
    val heparinContinuous: Float
)
