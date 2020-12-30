package blue.mild.breviary.backend.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * HeparinPatientWithDataDtoOut.
 */
data class HeparinPatientWithDataDtoOut(
    @JsonProperty(required = true)
    val heparinPatient: HeparinPatientDtoOut,

    @JsonProperty(required = true)
    val actualAptt: Float?,

    @JsonProperty(required = true)
    val actualApttUpdatedOn: Instant?,

    @JsonProperty(required = true)
    val previousAptt: Float?,

    @JsonProperty(required = true)
    val actualDosage: Float?,

    @JsonProperty(required = true)
    val previousDosage: Float?
)
