package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * InsulinPatientHistoryEntryDtoOut.
 */
data class InsulinPatientHistoryEntryDtoOut(
    @JsonProperty(required = true)
    val date: Instant,

    @JsonProperty(required = true)
    val dosage: Float,

    @JsonProperty(required = true)
    val carbohydrateIntake: Float,

    @JsonProperty(required = true)
    val glycemiaValue: Float
)
